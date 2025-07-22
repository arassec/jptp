package com.arassec.jptp.core.container;

import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.valuerange.ContainerType;
import com.arassec.jptp.core.datatype.valuerange.OperationCode;
import com.arassec.jptp.core.datatype.variable.SessionId;
import com.arassec.jptp.core.datatype.variable.TransactionId;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public record CommandContainer(
        UnsignedInt length,
        OperationCode operationCode,
        SessionId sessionId,
        TransactionId transactionId,
        UnsignedInt paramOne,
        UnsignedInt paramTwo,
        UnsignedInt paramThree) {

    // 4 Byte 'containerLength' + 2 Byte 'containerType' + 2 Byte 'OperationCode'
    private static final int BASE_HEADER_LENGTH = 8;


    public static CommandContainer newInstance(OperationCode operationCode,
                                               SessionId sessionId,
                                               TransactionId transactionId) {
        return newInstance(operationCode, sessionId, transactionId, null, null, null);
    }

    public static CommandContainer newInstance(OperationCode operationCode,
                                               SessionId sessionId,
                                               TransactionId transactionId,
                                               UnsignedInt paramOne) {
        return newInstance(operationCode, sessionId, transactionId, paramOne, null, null);
    }

    public static CommandContainer newInstance(OperationCode operationCode,
                                               SessionId sessionId,
                                               TransactionId transactionId,
                                               UnsignedInt paramOne,
                                               UnsignedInt paramTwo) {
        return newInstance(operationCode, sessionId, transactionId, paramOne, paramTwo, null);
    }

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
