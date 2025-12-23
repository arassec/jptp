package com.arasse.jptp.main;

import com.arasse.jptp.main.adapter.VendorAdapter;
import com.arasse.jptp.main.adapter.vendor.CanonVendorAdapter;
import com.arasse.jptp.main.adapter.vendor.DefaultVendorAdapter;
import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.PtpDeviceDiscovery;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.ResponseContainer;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.complex.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of an {@link ImageCaptureDevice}.
 * <p>
 * Uses the first PTP device found which is capable of capturing images.
 */
public class PtpImageCaptureDevice implements ImageCaptureDevice {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(PtpImageCaptureDevice.class);

    /**
     * A {@link PtpDeviceDiscovery} to find PTP devices.
     */
    private final PtpDeviceDiscovery ptpDeviceDiscovery;

    /**
     * List of all available vendor adapters.
     */
    private final List<VendorAdapter> vendorAdapters = List.of(
            new DefaultVendorAdapter(),
            new CanonVendorAdapter()
    );

    /**
     * Stores whether the class has been initialized or not.
     */
    private boolean initialized = false;

    /**
     * The first {@link PtpDevice} found which is capable of capturing images.
     */
    private PtpDevice ptpDevice;

    /**
     * The {@link DeviceInfo} of the selected PTP device.
     */
    private DeviceInfo selectedDeviceInfo;

    /**
     * Contains all existing objects on the device. Used to circumvent non-standard behavior, when 'initiate capture' is
     * not responded with the events specified in the standard.
     */
    private List<ObjectHandle> existingObjects;

    /**
     * The selected vendor adapter.
     */
    private VendorAdapter selectedVendorAdapter;

    /**
     * Creates a new instance.
     *
     * @param ptpDeviceDiscovery A discovery for PTP devices.
     */
    public PtpImageCaptureDevice(PtpDeviceDiscovery ptpDeviceDiscovery) {
        this.ptpDeviceDiscovery = ptpDeviceDiscovery;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean initialize() {
        teardown();
        ptpDeviceDiscovery.initialize();

        for (PtpDevice device : ptpDeviceDiscovery.discoverPtpDevices()) {
            Optional<DeviceInfo> optionalDeviceInfo = initializeDevice(device);
            if (optionalDeviceInfo.isPresent()) {
                selectedDeviceInfo = optionalDeviceInfo.get();
                ptpDevice = device;
                initialized = true;
                log.info("PTP device found and initialized: {} - {}",
                        selectedDeviceInfo.manufacturer(), selectedDeviceInfo.model());
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean initialize(Duration defaultTimeout, Duration eventTimeout) {
        boolean initSuccess = initialize();
        if (initSuccess) {
            ptpDevice.setDefaultTimeoutInMillis(defaultTimeout.toMillis());
            ptpDevice.setEventTimeoutInMillis(eventTimeout.toMillis());
        }
        return initSuccess;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void teardown() {
        if (initialized) {
            ResponseContainer responseContainer = ptpDevice
                    .sendCommand(CommandContainer.newInstance(OperationCode.CLOSE_SESSION, null, ptpDevice.incrementTransactionId()), null)
                    .responseContainer();

            if (!ResponseCode.OK.equals(responseContainer.responseCode())) {
                throw new IllegalStateException("Could not close PTP session!");
            }

            ptpDevice.teardown();
            ptpDeviceDiscovery.teardown();

            initialized = false;
            selectedDeviceInfo = null;
            ptpDevice = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DeviceInfo> getDeviceInfo() {
        if (!initialized) {
            return Optional.empty();
        }

        return Optional.ofNullable(selectedDeviceInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DataObject> captureImage() {
        if (!initialized) {
            throw new IllegalStateException("Device has not been initialized!");
        }

        existingObjects = getObjectHandles().handles();

        ResponseCode responseCode = selectedVendorAdapter.sendCaptureCommands(ptpDevice);
        if (!ResponseCode.OK.equals(responseCode)) {
            throw new IllegalStateException("Could not initiate capture mode!");
        }

        return retrieveNewDataObject();
    }

    /**
     * Tries to initialize the provided device by retrieving its device info and opening a new session.
     * Required PTP operations are also checked to be supported by the device.
     *
     * @param ptpDevice The {@link PtpDevice} to initialize.
     * @return The device's {@link DeviceInfo} if the device is supported and could be initialized or an empty
     * {@link Optional} instead.
     */
    private Optional<DeviceInfo> initializeDevice(PtpDevice ptpDevice) {
        ptpDevice.initialize();

        CommandResult<DeviceInfo> deviceInfoCommandResult = ptpDevice.sendCommand(
                CommandContainer.newInstance(OperationCode.GET_DEVICE_INFO, ptpDevice.getSessionId(), ptpDevice.incrementTransactionId()),
                DeviceInfo.DESERIALIZER);

        if (deviceInfoCommandResult == null
                || deviceInfoCommandResult.responseContainer() == null
                || !ResponseCode.OK.equals(deviceInfoCommandResult.responseContainer().responseCode())) {
            log.debug("PTP device found but DeviceInfo could not be retrieved: {}", deviceInfoCommandResult);
            return Optional.empty();
        } else {
            DeviceInfo deviceInfo = deviceInfoCommandResult.dataContainer().payload();

            Optional<VendorAdapter> optionalVendorAdapter = vendorAdapters.stream()
                    .filter(adapter -> adapter.supports(deviceInfo))
                    .findFirst();

            if (optionalVendorAdapter.isEmpty()) {
                log.debug("PTP device does not support required operations!");
                return Optional.empty();
            } else {
                selectedVendorAdapter = optionalVendorAdapter.get();
                log.debug("Selected vendor adapter: {}", selectedVendorAdapter.getClass().getSimpleName());
            }

            ResponseContainer responseContainer = ptpDevice
                    .sendCommand(CommandContainer.newInstance(OperationCode.OPEN_SESSION, ptpDevice.incrementSessionId(), ptpDevice.incrementTransactionId()), null)
                    .responseContainer();

            if (!ResponseCode.OK.equals(responseContainer.responseCode())) {
                throw new IllegalStateException("Could not open PTP session!");
            }

            return Optional.of(deviceInfo);
        }
    }

    /**
     * Polls for a new image on the device. If found, downloads the image from the device, deletes it on the device and
     * returns the object.
     * <p>
     * Not as elegant as it could be, but Nikon does not seem to send the events described in the PTP specification.
     *
     * @return The newly created {@link DataObject} if it exists yet.
     */
    private Optional<DataObject> retrieveNewDataObject() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted during image retrieval!", e);
            }

            Optional<ObjectHandle> newestObject = getNewestObject();

            if (newestObject.isPresent()) {

                CommandResult<DataObject> result = ptpDevice.sendCommand(
                        CommandContainer.newInstance(OperationCode.GET_OBJECT, null, ptpDevice.incrementTransactionId(), newestObject.get().handle()),
                        DataObject.DESERIALIZER);
                if (!ResponseCode.OK.equals(result.responseContainer().responseCode())) {
                    throw new IllegalStateException("Could not get captured image!");
                }

                ResponseContainer deleteObjectResponse = ptpDevice.sendCommand(
                        CommandContainer.newInstance(OperationCode.DELETE_OBJECT, null, ptpDevice.incrementTransactionId(), newestObject.get().handle()),
                        null).responseContainer();
                if (!ResponseCode.OK.equals(deleteObjectResponse.responseCode())) {
                    throw new IllegalStateException("Could not delete newly captured image from device!");
                }

                return Optional.of(result.dataContainer().payload());
            }
        }

        return Optional.empty();
    }

    /**
     * Returns the newest object found on the device.
     *
     * @return The newest object.
     */
    private Optional<ObjectHandle> getNewestObject() {
        List<ObjectHandle> currentHandles = getObjectHandles().handles();

        currentHandles.removeAll(existingObjects);

        if (currentHandles.isEmpty()) {
            return Optional.empty();
        }

        if (currentHandles.size() != 1) {
            throw new IllegalStateException("Expected 1 object handle but got " + currentHandles.size());
        }

        return Optional.of(currentHandles.getFirst());
    }

    /**
     * Returns all existing objects that are stored on the device.
     *
     * @return All objects stored on the device.
     */
    private ObjectHandleArray getObjectHandles() {
        CommandContainer getObjectHandlesCommand = CommandContainer.newInstance(OperationCode.GET_OBJECT_HANDLES, null, ptpDevice.incrementTransactionId(), UnsignedInt.maxInstance());
        CommandResult<ObjectHandleArray> result = ptpDevice.sendCommand(getObjectHandlesCommand, ObjectHandleArray.DESERIALIZER);
        if (!ResponseCode.OK.equals(result.responseContainer().responseCode())) {
            throw new IllegalStateException("Could not load object handles!");
        }
        return result.dataContainer().payload();
    }

}
