package com.arassec.jptp.core.container;

import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.valuerange.ContainerType;
import com.arassec.jptp.core.datatype.valuerange.OperationCode;
import com.arassec.jptp.core.datatype.simple.SessionId;
import com.arassec.jptp.core.datatype.simple.TransactionId;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A command container which is used to send commands to a PTP device.
 *
 * @param length        The total length of the container in bytes.
 * @param operationCode The operation code to execute.
 * @param sessionId     The PTP session ID.
 * @param transactionId The PTP transaction ID.
 * @param paramOne      The first command parameter.
 * @param paramTwo      The second command parameter.
 * @param paramThree    The third command parameter.
 */
public record CommandContainer(
        UnsignedInt length,
        OperationCode operationCode,
        SessionId sessionId,
        TransactionId transactionId,
        UnsignedInt paramOne,
        UnsignedInt paramTwo,
        UnsignedInt paramThree) {

    /**
     * The header length of every command container:
     * 4 Byte 'containerLength' + 2 Byte 'containerType' + 2 Byte 'OperationCode'
     */
    private static final int BASE_HEADER_LENGTH = 8;

    /**
     * Creates a new instance without parameters.
     *
     * @param operationCode The operation code to use.
     * @param sessionId     The session ID to use.
     * @param transactionId The transaction ID to use.
     * @return A new {@link CommandContainer} instance.
     */
    public static CommandContainer newInstance(OperationCode operationCode,
                                               SessionId sessionId,
                                               TransactionId transactionId) {
        return newInstance(operationCode, sessionId, transactionId, null, null, null);
    }

    /**
     * Creates a new instance with one parameter.
     *
     * @param operationCode The operation code to use.
     * @param sessionId     The session ID to use.
     * @param transactionId The transaction ID to use.
     * @param paramOne      The first parameter to use.
     * @return A new {@link CommandContainer} instance.
     */
    public static CommandContainer newInstance(OperationCode operationCode,
                                               SessionId sessionId,
                                               TransactionId transactionId,
                                               UnsignedInt paramOne) {
        return newInstance(operationCode, sessionId, transactionId, paramOne, null, null);
    }

    /**
     * Creates a new instance with two parameters.
     *
     * @param operationCode The operation code to use.
     * @param sessionId     The session ID to use.
     * @param transactionId The transaction ID to use.
     * @param paramOne      The first parameter to use.
     * @param paramTwo      The second parameter to use.
     * @return A new {@link CommandContainer} instance.
     */
    public static CommandContainer newInstance(OperationCode operationCode,
                                               SessionId sessionId,
                                               TransactionId transactionId,
                                               UnsignedInt paramOne,
                                               UnsignedInt paramTwo) {
        return newInstance(operationCode, sessionId, transactionId, paramOne, paramTwo, null);
    }

    /**
     * Creates a new instance with three parameters.
     *
     * @param operationCode The operation code to use.
     * @param sessionId     The session ID to use.
     * @param transactionId The transaction ID to use.
     * @param paramOne      The first parameter to use.
     * @param paramTwo      The second parameter to use.
     * @param paramThree    The third parameter to use.
     * @return A new {@link CommandContainer} instance.
     */
    public static CommandContainer newInstance(OperationCode operationCode,
                                               SessionId sessionId,
                                               TransactionId transactionId,
                                               UnsignedInt paramOne,
                                               UnsignedInt paramTwo,
                                               UnsignedInt paramThree) {

        int containerLength = BASE_HEADER_LENGTH
                + (sessionId != null ? 4 : 0)
                + (transactionId != null ? 4 : 0)
                + (paramOne != null ? 4 : 0)
                + (paramTwo != null ? 4 : 0)
                + (paramThree != null ? 4 : 0);

        return new CommandContainer(
                UnsignedInt.valueOf(containerLength),
                operationCode,
                sessionId,
                transactionId,
                paramOne,
                paramTwo,
                paramThree
        );
    }

    /**
     * Serializes this command container as byte buffer.
     *
     * @return The {@link ByteBuffer} representing this command.
     */
    public ByteBuffer serialize() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(length().value());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(length().value());                          // Length
        buffer.putShort(ContainerType.COMMAND.getType().value()); // Command
        buffer.putShort(operationCode.code().value());            // Operation code
        buffer.putInt(transactionId.id().value());                // Transaction ID
        if (sessionId != null) {
            buffer.putInt(sessionId.id().value());                // Session ID
        }

        if (paramOne != null) {
            buffer.putInt(paramOne.value());
            if (paramTwo != null) {
                buffer.putInt(paramTwo.value());
                if (paramThree != null) {
                    buffer.putInt(paramThree.value());
                }
            }
        }

        buffer.rewind();

        return buffer;
    }

}
