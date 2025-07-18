package com.arassec.jptp.core.container;

import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.valuerange.ContainerType;
import com.arassec.jptp.core.datatype.valuerange.ObjectFormatCode;
import com.arassec.jptp.core.datatype.valuerange.OperationCode;
import com.arassec.jptp.core.datatype.variable.ObjectHandle;
import com.arassec.jptp.core.datatype.variable.SessionId;
import com.arassec.jptp.core.datatype.variable.StorageId;
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

    public static CommandContainer getDeviceInfoCommand(SessionId sessionId, TransactionId transactionId) {
        return new CommandContainer(
                UnsignedInt.valueOf(28),
                OperationCode.GET_DEVICE_INFO,
                sessionId,
                transactionId,
                UnsignedInt.nullInstance(),
                UnsignedInt.nullInstance(),
                UnsignedInt.nullInstance()
        );
    }

    public static CommandContainer openSessionCommand(SessionId sessionId) {
        return new CommandContainer(
                UnsignedInt.valueOf(16),
                OperationCode.OPEN_SESSION,
                sessionId,
                new TransactionId(UnsignedInt.nullInstance()),
                null,
                null,
                null
        );
    }

    public static CommandContainer closeSessionCommand(TransactionId transactionId) {
        return new CommandContainer(
                UnsignedInt.valueOf(12),
                OperationCode.CLOSE_SESSION,
                null,
                transactionId,
                null,
                null,
                null
        );
    }

    public static CommandContainer getStorageIdsCommand(TransactionId transactionId) {
        return new CommandContainer(
                UnsignedInt.valueOf(12),
                OperationCode.GET_STORAGE_IDS,
                null,
                transactionId,
                null,
                null,
                null
        );
    }

    public static CommandContainer getStorageInfoCommand(TransactionId transactionId, StorageId storageId) {
        return new CommandContainer(
                UnsignedInt.valueOf(16),
                OperationCode.GET_STORAGE_INFO,
                null,
                transactionId,
                storageId.id(),
                null,
                null
        );
    }

    public static CommandContainer getObjectHandlesCommand(TransactionId transactionId, StorageId storageId, ObjectFormatCode objectFormatCode, ObjectHandle objectHandle) {
        return new CommandContainer(
                UnsignedInt.valueOf(16 + (objectFormatCode != null ? 4 : 0) + (objectHandle != null ? 4 : 0)),
                OperationCode.GET_OBJECT_HANDLES,
                null,
                transactionId,
                storageId.id(),
                objectFormatCode != null ? UnsignedInt.valueOf(objectFormatCode.code().value()) : null,
                objectHandle != null ? objectHandle.handle() : null
        );
    }

    public static CommandContainer getObjectInfoCommand(TransactionId transactionId, ObjectHandle objectHandle) {
        return new CommandContainer(
                UnsignedInt.valueOf(16),
                OperationCode.GET_OBJECT_INFO,
                null,
                transactionId,
                objectHandle.handle(),
                null,
                null
        );
    }

    public static CommandContainer getObjectCommand(TransactionId transactionId, ObjectHandle objectHandle) {
        return new CommandContainer(
                UnsignedInt.valueOf(16),
                OperationCode.GET_OBJECT,
                null,
                transactionId,
                objectHandle.handle(),
                null,
                null
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
