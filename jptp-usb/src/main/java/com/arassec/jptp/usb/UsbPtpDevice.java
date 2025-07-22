package com.arassec.jptp.usb;

import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.DataContainer;
import com.arassec.jptp.core.container.EventContainer;
import com.arassec.jptp.core.container.ResponseContainer;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.valuerange.ContainerType;
import com.arassec.jptp.core.datatype.variable.CommandResult;
import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.datatype.variable.SessionId;
import com.arassec.jptp.core.datatype.variable.TransactionId;
import com.arassec.jptp.usb.event.EventHandlingThread;
import com.arassec.jptp.usb.type.BulkInEndpointDescriptor;
import com.arassec.jptp.usb.type.BulkOutEndpointDescriptor;
import com.arassec.jptp.usb.type.InterruptEndpointDescriptor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.usb4java.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

@Slf4j
@RequiredArgsConstructor
public class UsbPtpDevice implements PtpDevice {

    private final ConfigDescriptor configDescriptor;

    private final DeviceHandle deviceHandle;

    private final BulkOutEndpointDescriptor bulkOut;

    private final BulkInEndpointDescriptor bulkIn;

    private final InterruptEndpointDescriptor interrupt;

    private final EventHandlingThread eventHandlingThread = new EventHandlingThread();

    private boolean initialized = false;

    private int transactionId = 0;

    private int sessionId = 0;

    @Setter
    private long defaultTimeoutInMillis = 1000;

    @Setter
    private long eventTimeoutInMillis = 5000;

    @Override
    public void initialize() {
        if (!initialized) {
            executeAndVerify(() -> LibUsb.controlTransfer(deviceHandle,
                    (byte) (LibUsb.ENDPOINT_OUT | LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE),
                    (byte) 0x66, (short) 0, (short) 0, ByteBuffer.allocateDirect(0), 1000), "Could not send 'reset' to USB device!");
            eventHandlingThread.start();
            initialized = true;
        }
    }

    @Override
    public void teardown() {
        if (initialized) {
            eventHandlingThread.abort();
            try {
                eventHandlingThread.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException("Could not join event handling thread!", e);
            }
            LibUsb.close(deviceHandle);
            LibUsb.freeConfigDescriptor(configDescriptor);
        }
    }

    @Override
    public SessionId getSessionId() {
        return new SessionId(UnsignedInt.valueOf(sessionId));
    }

    @Override
    public SessionId incrementSessionId() {
        sessionId++;
        return new SessionId(UnsignedInt.valueOf(sessionId));
    }

    @Override
    public TransactionId incrementTransactionId() {
        transactionId++;
        return new TransactionId(UnsignedInt.valueOf(transactionId));
    }

    @Override
    public <P extends PtpContainerPayload<P>> CommandResult<P> sendCommand(CommandContainer container, P payloadInstance) {
        if (!initialized) {
            throw new IllegalStateException("UsbPtpDevice has not been initialized!");
        }

        ByteBuffer commandBuffer = container.serialize();

        IntBuffer bytesTransferred = IntBuffer.allocate(1);
        executeAndVerify(() -> LibUsb.bulkTransfer(deviceHandle, bulkOut.descriptor().bEndpointAddress(), commandBuffer, bytesTransferred, defaultTimeoutInMillis), "Could not send command");
        log.trace("command sent ({} bytes): {}", bytesTransferred.get(0), container);

        ByteBuffer responseBuffer = readResponse(bulkIn.descriptor().bEndpointAddress(), defaultTimeoutInMillis);

        DataContainer<P> dataContainer = null;
        ResponseContainer responseContainer = null;

        if (ContainerType.DATA.equals(getContainerType(responseBuffer))) {
            dataContainer = DataContainer.deserialize(responseBuffer, payloadInstance);
            responseBuffer = readResponse(bulkIn.descriptor().bEndpointAddress(), defaultTimeoutInMillis);
            responseContainer = ResponseContainer.deserialize(responseBuffer);
        } else if (ContainerType.RESPONSE.equals(getContainerType(responseBuffer))) {
            responseContainer = ResponseContainer.deserialize(responseBuffer);
            if (payloadInstance != null) {
                responseBuffer = readResponse(bulkIn.descriptor().bEndpointAddress(), defaultTimeoutInMillis);
                dataContainer = DataContainer.deserialize(responseBuffer, payloadInstance);
            }
        } else {
            throw new IllegalStateException("Unexpected container type received: " + getContainerType(responseBuffer));
        }

        if (dataContainer != null) {
            log.debug("Data received: {}", dataContainer);
        }
        log.debug("Response received: {}", responseContainer);

        return new CommandResult<>(dataContainer, responseContainer);
    }

    @Override
    public <P extends PtpContainerPayload<P>> EventContainer<P> pollForEvent(P payloadInstance) {
        ByteBuffer responseBuffer = readResponse(interrupt.descriptor().bEndpointAddress(), eventTimeoutInMillis);

        if (!ContainerType.EVENT.equals(getContainerType(responseBuffer))) {
            throw new IllegalStateException("Unexpected container type received: " + getContainerType(responseBuffer));
        }

        return EventContainer.deserialize(responseBuffer, payloadInstance);
    }

    private ByteBuffer readResponse(byte bEndpointAddress, long timeoutInMillis) {
        final ByteBuffer initialBuffer = ByteBuffer.allocateDirect(bulkIn.descriptor().wMaxPacketSize());
        initialBuffer.order(ByteOrder.LITTLE_ENDIAN);

        final IntBuffer initialBytesTransferred = IntBuffer.allocate(1);
        executeAndVerify(() -> LibUsb.bulkTransfer(deviceHandle, bEndpointAddress, initialBuffer, initialBytesTransferred, timeoutInMillis), "Could not read response data");

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
        if (result < LibUsb.SUCCESS) {
            throw new LibUsbException(errorMessage, result);
        }
    }

}
