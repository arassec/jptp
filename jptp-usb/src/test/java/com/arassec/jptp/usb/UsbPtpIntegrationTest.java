package com.arassec.jptp.usb;


import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.ResponseContainer;
import com.arassec.jptp.core.datatype.valuerange.ObjectFormatCode;
import com.arassec.jptp.core.datatype.valuerange.OperationCode;
import com.arassec.jptp.core.datatype.valuerange.ResponseCode;
import com.arassec.jptp.core.datatype.variable.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@Disabled("Only for manual tests.")
class UsbPtpIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetDeviceInfo() {
        UsbPtpDeviceDiscovery deviceDiscovery = new UsbPtpDeviceDiscovery();
        deviceDiscovery.initialize();

        List<PtpDevice> ptpDevices = deviceDiscovery.discoverPtpDevices();

        ptpDevices.forEach(ptpDevice -> {
            ptpDevice.initialize();

            CommandResult<DeviceInfo> commandResult = ptpDevice.sendCommand(
                    CommandContainer.newInstance(OperationCode.GET_DEVICE_INFO, ptpDevice.getSessionId(), ptpDevice.incrementTransactionId(), null, null, null),
                    DeviceInfo.emptyInstance);

            try {
                log.info("Data from PTP device:\n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(commandResult.dataContainer()));
                log.info("Response from PTP device:\n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(commandResult.responseContainer()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            ptpDevice.teardown();
        });

        deviceDiscovery.teardown();
    }

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
                CommandResult<StorageIdArray> storageIdArrayCommandResult = ptpDevice.sendCommand(getStorageIdsCommand, StorageIdArray.emptyInstance);

                StorageId storageId = storageIdArrayCommandResult.dataContainer().payload().ids().getFirst();

                CommandContainer getStorageInfoCommand = CommandContainer.newInstance(OperationCode.GET_STORAGE_INFO, null, ptpDevice.incrementTransactionId(), storageId.id());
                CommandResult<StorageInfo> storageInfoCommandResult = ptpDevice.sendCommand(getStorageInfoCommand, StorageInfo.emptyInstance);
                log.info("Storage type: {}", storageInfoCommandResult.dataContainer().payload().storageType());

                CommandContainer getObjectHandlesCommand = CommandContainer.newInstance(OperationCode.GET_OBJECT_HANDLES, null, ptpDevice.incrementTransactionId(), storageId.id());
                CommandResult<ObjectHandleArray> objectHandlesCommandResult = ptpDevice.sendCommand(getObjectHandlesCommand, ObjectHandleArray.emptyInstance);

                ObjectHandle objectHandle = null;
                for (ObjectHandle possibleObjectHandle : objectHandlesCommandResult.dataContainer().payload().handles()) {
                    CommandContainer getObjectInfoCommand = CommandContainer.newInstance(OperationCode.GET_OBJECT_INFO, null, ptpDevice.incrementTransactionId(), possibleObjectHandle.handle());
                    CommandResult<ObjectInfo> objectInfoCommandResult = ptpDevice.sendCommand(getObjectInfoCommand, ObjectInfo.emptyInstance);

                    if (ObjectFormatCode.EXIF_JPEG.equals(objectInfoCommandResult.dataContainer().payload().objectFormat())) {
                        log.info("File found: {}", objectInfoCommandResult.dataContainer().payload().filename().name().rawValue());
                        objectHandle = possibleObjectHandle;
                        break;
                    }
                }

                if (objectHandle != null) {
                    CommandContainer getObjectCommand = CommandContainer.newInstance(OperationCode.GET_OBJECT, null, ptpDevice.incrementTransactionId(), objectHandle.handle());
                    CommandResult<DataObject> objectCommandResult = ptpDevice.sendCommand(getObjectCommand, DataObject.emptyInstance);

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
