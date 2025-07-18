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

public class UsbPtpDeviceDiscovery implements PtpDeviceDiscovery {

    private static final int DEVICE_CLASS_ZERO = 0;

    private static final byte INTERFACE_CLASS_IMAGING = 0x06;

    private static final byte INTERFACE_SUBCLASS_STILL_IMAGE_CAPTURE = 0x01;

    private static final byte INTERFACE_PROTOCOL_PTP = 0x01;

    private final DeviceList deviceList = new DeviceList();

    private boolean initialized = false;

    @Override
    public void initialize() {
        executeAndVerify(() -> LibUsb.init(null), "Unable to initialize LibUsb");
        initialized = true;
    }

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

            if (deviceDescriptor.bDeviceClass() == DEVICE_CLASS_ZERO) {
                isPtpDevice(device).ifPresent(ptpDevices::add);
            }

        });

        return ptpDevices;
    }

    @Override
    public void teardown() {
        LibUsb.freeDeviceList(deviceList, true);
        LibUsb.exit(null);
    }

    private Optional<UsbPtpDevice> isPtpDevice(Device device) {

        AtomicReference<Optional<UsbPtpDevice>> result = new AtomicReference<>(Optional.empty());

        ConfigDescriptor configDescriptor = new ConfigDescriptor();
        executeAndVerify(() -> LibUsb.getActiveConfigDescriptor(device, configDescriptor), "Unable to retrieve USB config descriptor");

        Arrays.stream(configDescriptor.iface()).forEach(usbInterface ->
                Arrays.stream(usbInterface.altsetting()).forEach(usbInterfaceDescriptor -> {

                    byte interfaceClass = usbInterfaceDescriptor.bInterfaceClass();
                    byte interfaceSubClass = usbInterfaceDescriptor.bInterfaceSubClass();
                    byte interfaceProtocol = usbInterfaceDescriptor.bInterfaceProtocol();

                    if (interfaceClass == INTERFACE_CLASS_IMAGING
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
                                new UsbPtpDevice(device, configDescriptor, deviceHandle, new BulkOutEndpointDescriptor(bulkOutEp),
                                        new BulkInEndpointDescriptor(bulkInEp), new InterruptEndpointDescriptor(interruptEp))
                        ));
                    }
                })
        );

        return result.get();
    }

    private Optional<EndpointDescriptor> getEndpointDescriptor(List<EndpointDescriptor> endpointDescriptors, byte transferType, byte endpointType) {
        for (EndpointDescriptor endpointDescriptor : endpointDescriptors) {
            if (transferType == (endpointDescriptor.bmAttributes() & LibUsb.TRANSFER_TYPE_MASK)
                    && endpointType == (endpointDescriptor.bEndpointAddress() & LibUsb.ENDPOINT_DIR_MASK)) {
                return Optional.of(endpointDescriptor);
            }
        }
        return Optional.empty();
    }


    private void executeAndVerify(UsbMethodExecutor usbExecutor, String errorMessage) {
        int result = usbExecutor.execute();
        if (result < LibUsb.SUCCESS) {
            throw new LibUsbException(errorMessage, result);
        }
    }

}
