package com.arassec.jptp.usb;

import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.PtpDeviceDiscovery;
import com.arassec.jptp.usb.type.BulkInEndpointDescriptor;
import com.arassec.jptp.usb.type.BulkOutEndpointDescriptor;
import com.arassec.jptp.usb.type.InterruptEndpointDescriptor;
import org.usb4java.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implements a {@link PtpDeviceDiscovery} using USB as transport layer.
 */
public class UsbPtpDeviceDiscovery implements PtpDeviceDiscovery {

    /**
     * USB subclass for still image devices.
     */
    private static final byte INTERFACE_SUBCLASS_STILL_IMAGE_CAPTURE = 0x01;

    /**
     * Required interface protocol PTP.
     */
    private static final byte INTERFACE_PROTOCOL_PTP = 0x01;

    /**
     * Contains all found USB devices that match the required criteria.
     */
    private final DeviceList deviceList = new DeviceList();

    /**
     * Keeps track of this classes initialization state.
     */
    private boolean initialized = false;

    /**
     * Creates a new, uninitialized instance.
     */
    public UsbPtpDeviceDiscovery() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        if (!initialized) {
            executeAndVerify(() -> LibUsb.init(null), "Unable to initialize LibUsb");
            initialized = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PtpDevice> discoverPtpDevices() {
        if (!initialized) {
            throw new IllegalStateException("Device discovery not initialized");
        }

        List<PtpDevice> ptpDevices = new ArrayList<>();

        executeAndVerify(() -> LibUsb.getDeviceList(null, deviceList), "Unable to retrieve USB device list");

        deviceList.forEach(device -> {

            DeviceDescriptor deviceDescriptor = new DeviceDescriptor();
            executeAndVerify(() -> LibUsb.getDeviceDescriptor(device, deviceDescriptor), "Unable to retrieve USB device descriptor");

            if (deviceDescriptor.bDeviceClass() == LibUsb.CLASS_PER_INTERFACE) {
                isPtpDevice(device).ifPresent(ptpDevices::add);
            }

        });

        return ptpDevices;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void teardown() {
        if (initialized) {
            LibUsb.freeDeviceList(deviceList, true);
            LibUsb.exit(null);
        }
    }

    /**
     * Checks whether the supplied USB device matches the criteria required for PTP.
     *
     * @param device The USB device to check.
     * @return An optional containing the {@link UsbPtpDevice} if the supplied device is capable of PTP. An empty
     * optional otherwise.
     */
    private Optional<UsbPtpDevice> isPtpDevice(Device device) {

        AtomicReference<Optional<UsbPtpDevice>> result = new AtomicReference<>(Optional.empty());

        ConfigDescriptor configDescriptor = new ConfigDescriptor();
        executeAndVerify(() -> LibUsb.getActiveConfigDescriptor(device, configDescriptor), "Unable to retrieve USB config descriptor");

        Arrays.stream(configDescriptor.iface()).forEach(usbInterface ->
                Arrays.stream(usbInterface.altsetting()).forEach(usbInterfaceDescriptor -> {

                    byte interfaceClass = usbInterfaceDescriptor.bInterfaceClass();
                    byte interfaceSubClass = usbInterfaceDescriptor.bInterfaceSubClass();
                    byte interfaceProtocol = usbInterfaceDescriptor.bInterfaceProtocol();

                    if (interfaceClass == LibUsb.CLASS_IMAGE
                            && interfaceSubClass == INTERFACE_SUBCLASS_STILL_IMAGE_CAPTURE
                            && interfaceProtocol == INTERFACE_PROTOCOL_PTP) {

                        List<EndpointDescriptor> endpointDescriptors = Arrays.asList(usbInterfaceDescriptor.endpoint());

                        EndpointDescriptor bulkOutEp = getEndpointDescriptor(endpointDescriptors, LibUsb.TRANSFER_TYPE_BULK, LibUsb.ENDPOINT_OUT)
                                .orElseThrow(() -> new IllegalStateException("Could not find 'bulk out' endpoint descriptor"));
                        EndpointDescriptor bulkInEp = getEndpointDescriptor(endpointDescriptors, LibUsb.TRANSFER_TYPE_BULK, LibUsb.ENDPOINT_IN)
                                .orElseThrow(() -> new IllegalStateException("Could not find 'bulk in' endpoint descriptor"));
                        EndpointDescriptor interruptEp = getEndpointDescriptor(endpointDescriptors, LibUsb.TRANSFER_TYPE_INTERRUPT, LibUsb.ENDPOINT_IN)
                                .orElseThrow(() -> new IllegalStateException("Could not find 'interrupt' endpoint descriptor"));

                        DeviceHandle deviceHandle = new DeviceHandle();
                        executeAndVerify(() -> LibUsb.open(device, deviceHandle), "Unable to open USB device handle");

                        result.set(Optional.of(
                                new UsbPtpDevice(configDescriptor, deviceHandle, new BulkOutEndpointDescriptor(bulkOutEp),
                                        new BulkInEndpointDescriptor(bulkInEp), new InterruptEndpointDescriptor(interruptEp))
                        ));
                    }
                })
        );

        return result.get();
    }

    /**
     * Determines the requested endpoint descriptor.
     *
     * @param endpointDescriptors All endpoint descriptors of an USB device.
     * @param transferType        The required LibUSB transfer type.
     * @param endpointType        THe required LibUSB endpoint type.
     * @return An optional containing a matching {@link EndpointDescriptor} or an empty optional, if no descriptor
     * matches the requested criteria.
     */
    private Optional<EndpointDescriptor> getEndpointDescriptor(List<EndpointDescriptor> endpointDescriptors, byte transferType, byte endpointType) {
        for (EndpointDescriptor endpointDescriptor : endpointDescriptors) {
            if (transferType == (endpointDescriptor.bmAttributes() & LibUsb.TRANSFER_TYPE_MASK)
                    && endpointType == (endpointDescriptor.bEndpointAddress() & LibUsb.ENDPOINT_DIR_MASK)) {
                return Optional.of(endpointDescriptor);
            }
        }
        return Optional.empty();
    }

    /**
     * Executes a LibUSB operation and checks, that the returned result code is not an error code.
     *
     * @param usbExecutor  The callback the actually executes the operation.
     * @param errorMessage The error message to use in the exception that is thrown if the result code denotes an error.
     */
    private void executeAndVerify(UsbMethodExecutor usbExecutor, String errorMessage) {
        int result = usbExecutor.execute();
        if (result < LibUsb.SUCCESS) {
            throw new LibUsbException(errorMessage, result);
        }
    }

}
