package com.arassec.jptp.usb;


import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.ResponseContainer;
import com.arassec.jptp.core.datatype.complex.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Integration tests for {@link UsbPtpDevice}s. Only for manual use.
 */
@Disabled("Only for manual tests.")
class UsbPtpIntegrationTest {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(UsbPtpIntegrationTest.class);

    /**
     * Test getting device info.
     */
    @Test
    void testGetDeviceInfo() {
        UsbPtpDeviceDiscovery deviceDiscovery = new UsbPtpDeviceDiscovery();
        deviceDiscovery.initialize();

        List<PtpDevice> ptpDevices = deviceDiscovery.discoverPtpDevices();

        ptpDevices.forEach(ptpDevice -> {
            ptpDevice.initialize();

            CommandResult<DeviceInfo> commandResult = ptpDevice.sendCommand(
                    CommandContainer.newInstance(OperationCode.GET_DEVICE_INFO, ptpDevice.getSessionId(), ptpDevice.incrementTransactionId()),
                    DeviceInfo.DESERIALIZER);

            log.info("Data from PTP device: {}", commandResult.dataContainer());
            log.info("Response from PTP device: {}", commandResult.responseContainer());

            ptpDevice.teardown();
        });

        deviceDiscovery.teardown();
    }

    /**
     * Tests session management.
     */
    @Test
    void testSessionManagement() {
        UsbPtpDeviceDiscovery deviceDiscovery = new UsbPtpDeviceDiscovery();
        deviceDiscovery.initialize();

        List<PtpDevice> ptpDevices = deviceDiscovery.discoverPtpDevices();
        if (ptpDevices.isEmpty()) {
            fail("No PTP device found");
        }

        AtomicBoolean testSuccess = new AtomicBoolean(false);

        PtpDevice ptpDevice = ptpDevices.getFirst();

        ptpDevice.initialize();

        ResponseContainer responseContainer = ptpDevice
                .sendCommand(CommandContainer.newInstance(OperationCode.OPEN_SESSION, ptpDevice.incrementSessionId(), ptpDevice.incrementTransactionId()), null)
                .responseContainer();

        if (ResponseCode.OK.equals(responseContainer.responseCode())) {

            try {
                CommandContainer getStorageIdsCommand = CommandContainer.newInstance(OperationCode.GET_STORAGE_IDS, null, ptpDevice.incrementTransactionId());
                CommandResult<StorageIdArray> storageIdArrayCommandResult = ptpDevice.sendCommand(getStorageIdsCommand, StorageIdArray.DESERIALIZER);

                StorageId storageId = storageIdArrayCommandResult.dataContainer().payload().ids().getFirst();

                CommandContainer getStorageInfoCommand = CommandContainer.newInstance(OperationCode.GET_STORAGE_INFO, null, ptpDevice.incrementTransactionId(), storageId.id());
                CommandResult<StorageInfo> storageInfoCommandResult = ptpDevice.sendCommand(getStorageInfoCommand, StorageInfo.DESERIALIZER);
                log.info("Storage type: {}", storageInfoCommandResult.dataContainer().payload().storageType());

                CommandContainer getObjectHandlesCommand = CommandContainer.newInstance(OperationCode.GET_OBJECT_HANDLES, null, ptpDevice.incrementTransactionId(), storageId.id());
                CommandResult<ObjectHandleArray> objectHandlesCommandResult = ptpDevice.sendCommand(getObjectHandlesCommand, ObjectHandleArray.DESERIALIZER);

                ObjectHandle objectHandle = null;
                for (ObjectHandle possibleObjectHandle : objectHandlesCommandResult.dataContainer().payload().handles()) {
                    CommandContainer getObjectInfoCommand = CommandContainer.newInstance(OperationCode.GET_OBJECT_INFO, null, ptpDevice.incrementTransactionId(), possibleObjectHandle.handle());
                    CommandResult<ObjectInfo> objectInfoCommandResult = ptpDevice.sendCommand(getObjectInfoCommand, ObjectInfo.DESERIALIZER);

                    if (ObjectFormatCode.EXIF_JPEG.equals(objectInfoCommandResult.dataContainer().payload().objectFormat())) {
                        log.info("File found: {}", objectInfoCommandResult.dataContainer().payload().filename().name().value());
                        objectHandle = possibleObjectHandle;
                        break;
                    }
                }

                if (objectHandle != null) {
                    CommandContainer getObjectCommand = CommandContainer.newInstance(OperationCode.GET_OBJECT, null, ptpDevice.incrementTransactionId(), objectHandle.handle());
                    CommandResult<DataObject> objectCommandResult = ptpDevice.sendCommand(getObjectCommand, DataObject.DESERIALIZER);

                    try {
                        Files.write(Path.of("target/test.jpg"), objectCommandResult.dataContainer().payload().data());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            } finally {
                responseContainer = ptpDevice
                        .sendCommand(CommandContainer.newInstance(OperationCode.CLOSE_SESSION, null, ptpDevice.incrementTransactionId()), null)
                        .responseContainer();

                if (ResponseCode.OK.equals(responseContainer.responseCode())) {
                    testSuccess.set(true);
                }
            }
        }

        ptpDevice.teardown();

        deviceDiscovery.teardown();

        assertThat(testSuccess.get()).isTrue();
    }

}
