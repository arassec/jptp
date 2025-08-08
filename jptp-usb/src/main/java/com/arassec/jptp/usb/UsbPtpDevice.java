package com.arassec.jptp.usb;

import com.arassec.jptp.core.PayloadDeserializer;
import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.DataContainer;
import com.arassec.jptp.core.container.EventContainer;
import com.arassec.jptp.core.container.ResponseContainer;
import com.arassec.jptp.core.datatype.ContainerType;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.complex.CommandResult;
import com.arassec.jptp.core.datatype.complex.SessionId;
import com.arassec.jptp.core.datatype.complex.TransactionId;
import com.arassec.jptp.usb.type.BulkInEndpointDescriptor;
import com.arassec.jptp.usb.type.BulkOutEndpointDescriptor;
import com.arassec.jptp.usb.type.InterruptEndpointDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usb4java.ConfigDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Implements a {@link PtpDevice} using USB as transport layer.
 */
public class UsbPtpDevice implements PtpDevice {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(UsbPtpDevice.class);

    /**
     * LibUSB {@link ConfigDescriptor} for the used USB device.
     */
    private final ConfigDescriptor configDescriptor;

    /**
     * LibUSB {@link DeviceHandle} to the used USB device.
     */
    private final DeviceHandle deviceHandle;

    /**
     * Output channel to the selected USB device.
     */
    private final BulkOutEndpointDescriptor bulkOut;

    /**
     * Input channel from the selected USB device.
     */
    private final BulkInEndpointDescriptor bulkIn;

    /**
     * Interrupt channel of the selected USB device for events.
     */
    private final InterruptEndpointDescriptor interruptIn;

    /**
     * Holds whether the class has been initialized or not.
     */
    private boolean initialized = false;

    /**
     * The PTP transaction ID. Always incremented...
     */
    private int transactionId = 0;

    /**
     * The PTP session ID. Must be incremented when calling an OpenSession operation.
     */
    private int sessionId = 0;

    /**
     * Default timeout for USB device communication.
     */
    private long defaultTimeoutInMillis = 1000;

    /**
     * Timeout for waiting for USB device events.
     */
    private long eventTimeoutInMillis = 5000;

    /**
     * Creates a new instance.
     *
     * @param configDescriptor The USB device's config descriptor.
     * @param deviceHandle     The USB device handle.
     * @param bulkOut          The output channel to the device.
     * @param bulkIn           The input channel from the device.
     * @param interruptIn      The interrupt channel of the device.
     */
    public UsbPtpDevice(ConfigDescriptor configDescriptor, DeviceHandle deviceHandle, BulkOutEndpointDescriptor bulkOut,
                        BulkInEndpointDescriptor bulkIn, InterruptEndpointDescriptor interruptIn) {
        this.configDescriptor = configDescriptor;
        this.deviceHandle = deviceHandle;
        this.bulkOut = bulkOut;
        this.bulkIn = bulkIn;
        this.interruptIn = interruptIn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        if (!initialized) {
            executeAndVerify(() -> LibUsb.controlTransfer(deviceHandle,
                    (byte) (LibUsb.ENDPOINT_OUT | LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE),
                    (byte) 0x66, (short) 0, (short) 0, ByteBuffer.allocateDirect(0), defaultTimeoutInMillis), "Could not send 'reset' to USB device!");
            initialized = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void teardown() {
        if (initialized) {
            LibUsb.close(deviceHandle);
            LibUsb.freeConfigDescriptor(configDescriptor);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionId getSessionId() {
        return new SessionId(UnsignedInt.valueOf(sessionId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionId incrementSessionId() {
        sessionId++;
        return new SessionId(UnsignedInt.valueOf(sessionId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransactionId incrementTransactionId() {
        transactionId++;
        return new TransactionId(UnsignedInt.valueOf(transactionId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> CommandResult<P> sendCommand(CommandContainer container, PayloadDeserializer<P> payloadDeserializer) {
        if (!initialized) {
            throw new IllegalStateException("UsbPtpDevice has not been initialized!");
        }

        ByteBuffer commandBuffer = container.serialize();

        IntBuffer bytesTransferred = IntBuffer.allocate(1);
        executeAndVerify(() -> LibUsb.bulkTransfer(deviceHandle, bulkOut.descriptor().bEndpointAddress(), commandBuffer, bytesTransferred, defaultTimeoutInMillis), "Could not send command");
        log.trace("command sent ({} bytes): {}", bytesTransferred.get(0), container);

        byte bEndpointAddress = bulkIn.descriptor().bEndpointAddress();
        short capacity = bulkIn.descriptor().wMaxPacketSize();

        ByteBuffer responseBuffer = readDataAndResponse(true, bEndpointAddress, capacity, defaultTimeoutInMillis);

        DataContainer<P> dataContainer = null;
        ResponseContainer responseContainer;

        if (ContainerType.DATA.equals(getContainerType(responseBuffer))) {
            dataContainer = DataContainer.deserialize(responseBuffer, payloadDeserializer);
            responseBuffer = readDataAndResponse(true, bEndpointAddress, capacity, defaultTimeoutInMillis);
            responseContainer = ResponseContainer.deserialize(responseBuffer);
        } else if (ContainerType.RESPONSE.equals(getContainerType(responseBuffer))) {
            responseContainer = ResponseContainer.deserialize(responseBuffer);
            if (payloadDeserializer != null) {
                responseBuffer = readDataAndResponse(true, bEndpointAddress, capacity, defaultTimeoutInMillis);
                dataContainer = DataContainer.deserialize(responseBuffer, payloadDeserializer);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> EventContainer<P> pollForEvent(PayloadDeserializer<P> payloadDeserializer) {
        ByteBuffer responseBuffer = readDataAndResponse(false, interruptIn.descriptor().bEndpointAddress(),
                interruptIn.descriptor().wMaxPacketSize(), eventTimeoutInMillis);

        if (!ContainerType.EVENT.equals(getContainerType(responseBuffer))) {
            throw new IllegalStateException("Unexpected container type received: " + getContainerType(responseBuffer));
        }

        return EventContainer.deserialize(responseBuffer, payloadDeserializer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultTimeoutInMillis(long defaultTimeoutInMillis) {
        this.defaultTimeoutInMillis = defaultTimeoutInMillis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEventTimeoutInMillis(long eventTimeoutInMillis) {
        this.eventTimeoutInMillis = eventTimeoutInMillis;
    }

    /**
     * Reads a response container and an optional data container as response to a command request.
     *
     * @param bulkTransfer     If set to {@code true}, USB bulk-transfer ist used. interrupt-transfer otherwise.
     * @param bEndpointAddress The USB device address to read the response from.
     * @param capacity         The maximum buffer size for a single read operation.
     * @param timeoutInMillis  The timeout to wait before aborting the read operation.
     * @return The read container data as {@link ByteBuffer}.
     */
    private ByteBuffer readDataAndResponse(boolean bulkTransfer, byte bEndpointAddress, int capacity, long timeoutInMillis) {
        final ByteBuffer initialBuffer = ByteBuffer.allocateDirect(capacity);
        initialBuffer.order(ByteOrder.LITTLE_ENDIAN);

        final IntBuffer initialBytesTransferred = IntBuffer.allocate(1);
        if (bulkTransfer) {
            executeAndVerify(() -> LibUsb.bulkTransfer(deviceHandle, bEndpointAddress, initialBuffer, initialBytesTransferred, timeoutInMillis), "Could not read response data");
        } else {
            executeAndVerify(() -> LibUsb.interruptTransfer(deviceHandle, bEndpointAddress, initialBuffer, initialBytesTransferred, timeoutInMillis), "Could not read interrupt data");
        }

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
            int bufferSize = capacity;
            if (remainingBytes < bufferSize) {
                bufferSize = remainingBytes;
            }

            ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
            IntBuffer bytesTransferred = IntBuffer.allocate(1);

            if (bulkTransfer) {
                executeAndVerify(() -> LibUsb.bulkTransfer(deviceHandle, bulkIn.descriptor().bEndpointAddress(), buffer, bytesTransferred, 1000), "Could not read response data");
            } else {
                executeAndVerify(() -> LibUsb.interruptTransfer(deviceHandle, bulkIn.descriptor().bEndpointAddress(), buffer, bytesTransferred, 1000), "Could not read interrupt data");
            }

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

    /**
     * Returns the container type based on the read bytes.
     *
     * @param buffer The container as read from the USB device as byte buffer.
     * @return The {@link ContainerType} as denoted by the fifth and sixth byte of the buffer.
     */
    private ContainerType getContainerType(ByteBuffer buffer) {
        buffer.getInt();
        ContainerType containerType = ContainerType.valueOf(UnsignedShort.deserialize(buffer));
        buffer.rewind();
        return containerType;
    }

    /**
     * Executes the USB operation and verifies a successful execution.
     *
     * @param usbExecutor  The callback executing the LibUSB operation.
     * @param errorMessage The error message to use in the exception that is thrown if the result is not {@link LibUsb#SUCCESS}.
     */
    private void executeAndVerify(UsbMethodExecutor usbExecutor, String errorMessage) {
        int result = usbExecutor.execute();
        if (result < LibUsb.SUCCESS) {
            throw new LibUsbException(errorMessage, result);
        }
    }

}
