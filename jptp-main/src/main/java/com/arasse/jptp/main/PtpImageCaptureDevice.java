package com.arasse.jptp.main;

import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.PtpDeviceDiscovery;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.EventContainer;
import com.arassec.jptp.core.container.ResponseContainer;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.valuerange.EventCode;
import com.arassec.jptp.core.datatype.valuerange.OperationCode;
import com.arassec.jptp.core.datatype.valuerange.ResponseCode;
import com.arassec.jptp.core.datatype.variable.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Default implementation of an {@link ImageCaptureDevice}.
 * <p>
 * Uses the first PTP device found which is capable of capturing images.
 */
@Slf4j
@RequiredArgsConstructor
public class PtpImageCaptureDevice implements ImageCaptureDevice {

    private final PtpDeviceDiscovery ptpDeviceDiscovery;

    private boolean initialized = false;

    private PtpDevice ptpDevice;

    private DeviceInfo selectedDeviceInfo;

    @Override
    public boolean initialize() {

        if (!initialized) {
            ptpDeviceDiscovery.initialize();

            for (PtpDevice device : ptpDeviceDiscovery.discoverPtpDevices()) {
                Optional<DeviceInfo> optionalDeviceInfo = initializeDevice(device);
                if (optionalDeviceInfo.isPresent()) {
                    selectedDeviceInfo = optionalDeviceInfo.get();
                    ptpDevice = device;
                    initialized = true;
                    log.info("PTP device found and initialized: {} - {}", selectedDeviceInfo.manufacturer().value().rawValue(),
                            selectedDeviceInfo.model().value().rawValue());
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void teardown() {
        if (initialized) {

            ResponseContainer responseContainer = ptpDevice
                    .sendCommand(CommandContainer.newInstance(OperationCode.CLOSE_SESSION, ptpDevice.incrementSessionId(), null), null)
                    .responseContainer();

            if (!ResponseCode.OK.equals(responseContainer.responseCode())) {
                throw new IllegalStateException("Could not close PTP session!");
            }

            if (ptpDevice != null) {
                ptpDevice.teardown();
            }

            ptpDeviceDiscovery.teardown();

            initialized = false;
            selectedDeviceInfo = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DeviceInfo> getDeviceInfo() {
        if (!initialized) {
            throw new IllegalStateException("Device has not been initialized!");
        }

        return Optional.ofNullable(selectedDeviceInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObject captureImage() {
        if (!initialized) {
            throw new IllegalStateException("Device has not been initialized!");
        }

        if (ptpDevice == null) {
            throw new IllegalStateException("No PTP device available!");
        }

        ResponseContainer responseContainer = ptpDevice.sendCommand(CommandContainer.newInstance(OperationCode.INITIATE_CAPTURE, ptpDevice.incrementSessionId(), null, UnsignedInt.nullInstance(), UnsignedInt.nullInstance()), null)
                .responseContainer();
        if (!ResponseCode.OK.equals(responseContainer.responseCode())) {
            throw new IllegalStateException("Could not initiate capture mode!");
        }

        // Capture finished:
        EventContainer<NoData> captureCompleteeventContainer = ptpDevice.pollForEvent(NoData.emptyInstance());
        if (!EventCode.CAPTURE_COMPLETE.equals(captureCompleteeventContainer.eventCode())) {
            throw new IllegalStateException("Unexpected event received: " + captureCompleteeventContainer);
        }

        // Object added:
        EventContainer<ObjectHandle> objectAddedEventContainer = ptpDevice.pollForEvent(ObjectHandle.emptyInstance());
        if (!EventCode.OBJECT_ADDED.equals(objectAddedEventContainer.eventCode())) {
            throw new IllegalStateException("Unexpected event received: " + objectAddedEventContainer);
        }

        CommandResult<DataObject> objectCommandResult = ptpDevice.sendCommand(
                CommandContainer.newInstance(OperationCode.GET_OBJECT, null, ptpDevice.incrementTransactionId(), objectAddedEventContainer.payload().handle()),
                DataObject.emptyInstance);
        if (!ResponseCode.OK.equals(responseContainer.responseCode())) {
            throw new IllegalStateException("Could not get captured image!");
        }

        // TODO: Maybe delete object?

        return objectCommandResult.dataContainer().payload();
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
                CommandContainer.newInstance(OperationCode.GET_DEVICE_INFO, ptpDevice.getSessionId(), ptpDevice.incrementTransactionId(), null, null, null),
                DeviceInfo.emptyInstance);

        if (deviceInfoCommandResult == null
                || deviceInfoCommandResult.responseContainer() == null
                || !ResponseCode.OK.equals(deviceInfoCommandResult.responseContainer().responseCode())) {
            log.warn("PTP device found but DeviceInfo could not be retrieved: {}", deviceInfoCommandResult);
            return Optional.empty();
        } else {
            DeviceInfo deviceInfo = deviceInfoCommandResult.dataContainer().payload();

            if (deviceDoesNotSupport(deviceInfo, OperationCode.INITIATE_CAPTURE) || deviceDoesNotSupport(deviceInfo, OperationCode.GET_OBJECT)) {
                log.warn("PTP device does not support required operations INITIATE_CAPTURE and GET_OBJECT!");
                return Optional.empty();
            }

            ResponseContainer responseContainer = ptpDevice
                    .sendCommand(CommandContainer.newInstance(OperationCode.OPEN_SESSION, ptpDevice.incrementSessionId(), null), null)
                    .responseContainer();

            if (!ResponseCode.OK.equals(responseContainer.responseCode())) {
                throw new IllegalStateException("Could not open PTP session!");
            }

            return Optional.of(deviceInfo);
        }
    }

    /**
     * Checks whether the supplied device supports the required operation.
     *
     * @param deviceInfo    The device's {@link DeviceInfo}.
     * @param operationCode The required {@link OperationCode}.
     * @return {@code true}, if the device supports the operation, {@code false} otherwise.
     */
    private boolean deviceDoesNotSupport(DeviceInfo deviceInfo, OperationCode operationCode) {
        return deviceInfo.operationsSupported().stream()
                .noneMatch(operationCode::equals);
    }

}
