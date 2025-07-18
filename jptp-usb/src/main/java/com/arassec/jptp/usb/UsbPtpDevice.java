package com.arassec.jptp.usb;

import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.DataContainer;
import com.arassec.jptp.core.container.ResponseContainer;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.valuerange.ContainerType;
import com.arassec.jptp.core.datatype.variable.*;
import com.arassec.jptp.usb.type.BulkInEndpointDescriptor;
import com.arassec.jptp.usb.type.BulkOutEndpointDescriptor;
import com.arassec.jptp.usb.type.InterruptEndpointDescriptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.usb4java.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

@Slf4j
@RequiredArgsConstructor
public class UsbPtpDevice implements PtpDevice {

    private final Device device;

    private final ConfigDescriptor configDescriptor;

    private final DeviceHandle deviceHandle;

    private final BulkOutEndpointDescriptor bulkOut;

    private final BulkInEndpointDescriptor bulkIn;

    private final InterruptEndpointDescriptor interrupt;

    private int transactionId = 0;

    private int sessionId = 0;

    @Override
    public void initialize() {
        executeAndVerify(() -> LibUsb.controlTransfer(deviceHandle,
                (byte) (LibUsb.ENDPOINT_OUT | LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE),
                (byte) 0x66, (short) 0, (short) 0, ByteBuffer.allocateDirect(0), 1000), "Could not send 'reset' to USB device!");
    }

    @Override
    public void teardown() {
        LibUsb.close(deviceHandle);
        LibUsb.freeConfigDescriptor(configDescriptor);
    }

    @Override
    public SessionId getSessionId() {
        return new SessionId(UnsignedInt.valueOf(sessionId));
    }

    @Override
    public TransactionId getTransactionId() {
        transactionId++;
        return new TransactionId(UnsignedInt.valueOf(transactionId));
    }

    @Override
    public CommandResult<DeviceInfo> getDeviceInfo() {
        CommandContainer deviceInfoCommand = CommandContainer.getDeviceInfoCommand(getSessionId(), getTransactionId());
        ByteBuffer commandBuffer = deviceInfoCommand.serialize();

        IntBuffer bytesTransferred = IntBuffer.allocate(1);
        executeAndVerify(() -> LibUsb.bulkTransfer(deviceHandle, bulkOut.descriptor().bEndpointAddress(), commandBuffer, bytesTransferred, 1000), "Could not send 'GetDeviceInfo' command");
        log.trace("GetDeviceInfo command - bytes sent : {}", bytesTransferred.get(0));

        ByteBuffer responseBuffer = readResponse();

        DataContainer<DeviceInfo> dataContainer = null;
        if (ContainerType.DATA.equals(getContainerType(responseBuffer))) {
            dataContainer = DataContainer.deserialize(responseBuffer, DeviceInfo.emptyInstance);
            responseBuffer = readResponse();
        }

        ResponseContainer responseContainer = ResponseContainer.deserialize(responseBuffer);
        log.debug("GetDeviceInfo response: {}", responseContainer.responseCode().description());

        return new CommandResult<>(dataContainer, responseContainer);
    }

    @Override
    public ResponseContainer openSession() {
        sessionId++;
        CommandContainer deviceInfoCommand = CommandContainer.openSessionCommand(getSessionId());
        ByteBuffer commandBuffer = deviceInfoCommand.serialize();

        IntBuffer bytesTransferred = IntBuffer.allocate(1);
        executeAndVerify(() -> LibUsb.bulkTransfer(deviceHandle, bulkOut.descriptor().bEndpointAddress(), commandBuffer, bytesTransferred, 1000), "Could not send 'OpenSession' command");
        log.trace("OpenSession command - bytes sent : {}", bytesTransferred.get(0));

        ByteBuffer responseBuffer = readResponse();

        ResponseContainer responseContainer = ResponseContainer.deserialize(responseBuffer);
        log.debug("OpenSession response: {}", responseContainer.responseCode().description());

        return responseContainer;
    }

    @Override
    public ResponseContainer closeSession() {
        CommandContainer deviceInfoCommand = CommandContainer.closeSessionCommand(getTransactionId());
        ByteBuffer commandBuffer = deviceInfoCommand.serialize();

        IntBuffer bytesTransferred = IntBuffer.allocate(1);
        executeAndVerify(() -> LibUsb.bulkTransfer(deviceHandle, bulkOut.descriptor().bEndpointAddress(), commandBuffer, bytesTransferred, 1000), "Could not send 'CloseSession' command");
        log.trace("CloseSession command - bytes sent : {}", bytesTransferred.get(0));

        ByteBuffer responseBuffer = readResponse();

        ResponseContainer responseContainer = ResponseContainer.deserialize(responseBuffer);
        log.debug("CloseSession response: {}", responseContainer.responseCode().description());

        return responseContainer;
    }

    @Override
    public <P extends DataContainerPayload<P>> CommandResult<P> sendCommand(CommandContainer container, P payloadInstance) {
        ByteBuffer commandBuffer = container.serialize();

        IntBuffer bytesTransferred = IntBuffer.allocate(1);
        executeAndVerify(() -> LibUsb.bulkTransfer(deviceHandle, bulkOut.descriptor().bEndpointAddress(), commandBuffer, bytesTransferred, 1000), "Could not send command");
        log.trace("command - bytes sent : {}", bytesTransferred.get(0));

        ByteBuffer responseBuffer = readResponse();

        DataContainer<P> dataContainer = null;
        ResponseContainer responseContainer = null;

        if (ContainerType.DATA.equals(getContainerType(responseBuffer))) {
            dataContainer = DataContainer.deserialize(responseBuffer, payloadInstance);
            responseBuffer = readResponse();
            responseContainer = ResponseContainer.deserialize(responseBuffer);
        } else if (ContainerType.RESPONSE.equals(getContainerType(responseBuffer))) {
            responseContainer = ResponseContainer.deserialize(responseBuffer);
            responseBuffer = readResponse();
            dataContainer = DataContainer.deserialize(responseBuffer, payloadInstance);
        } else {
            throw new IllegalStateException("Unexpected container type received: " + getContainerType(responseBuffer));
        }

        log.debug("Command response: {}", responseContainer.responseCode().description());

        return new CommandResult<>(dataContainer, responseContainer);
    }

    private ByteBuffer readResponse() {
        final ByteBuffer initialBuffer = ByteBuffer.allocateDirect(bulkIn.descriptor().wMaxPacketSize());
        initialBuffer.order(ByteOrder.LITTLE_ENDIAN);

        final IntBuffer initialBytesTransferred = IntBuffer.allocate(1);
        executeAndVerify(() -> LibUsb.bulkTransfer(deviceHandle, bulkIn.descriptor().bEndpointAddress(), initialBuffer, initialBytesTransferred, 1000), "Could not read response data");

        int bytesReceived = initialBytesTransferred.get(0);
        log.trace("Read response - bytes received : {}", bytesReceived);

        int containerLength = initialBuffer.getInt();
        byte[] containerBuffer = new byte[containerLength + 4];

        initialBuffer.rewind();

        int index = 0;
        for (int i = 0; i < bytesReceived; i++) {
            containerBuffer[index] = initialBuffer.get();
            index++;
        }

        int remainingBytes = containerLength - bytesReceived;
        while (remainingBytes > 0) {
            int bufferSize = bulkIn.descriptor().wMaxPacketSize();
            if (remainingBytes < bufferSize) {
                bufferSize = remainingBytes;
            }

            ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
            IntBuffer bytesTransferred = IntBuffer.allocate(1);

            executeAndVerify(() -> LibUsb.bulkTransfer(deviceHandle, bulkIn.descriptor().bEndpointAddress(), buffer, bytesTransferred, 1000), "Could not read response data");
            bytesReceived = bytesTransferred.get(0);
            log.trace("Read response - bytes received : {}", bytesReceived);

            for (int i = 0; i < bytesReceived; i++) {
                containerBuffer[index] = buffer.get();
                index++;
            }

            remainingBytes -= bytesReceived;
        }

        ByteBuffer resultBuffer = ByteBuffer.wrap(containerBuffer);
        resultBuffer.order(ByteOrder.LITTLE_ENDIAN);

        return resultBuffer;
    }

    private ContainerType getContainerType(ByteBuffer buffer) {
        buffer.getInt();
        ContainerType containerType = ContainerType.valueOf(UnsignedShort.deserialize(buffer));
        buffer.rewind();
        return containerType;
    }

    private void executeAndVerify(UsbMethodExecutor usbExecutor, String errorMessage) {
        int result = usbExecutor.execute();
        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException(errorMessage, result);
        }
    }

}
