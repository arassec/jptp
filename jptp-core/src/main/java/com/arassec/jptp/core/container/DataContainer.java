package com.arassec.jptp.core.container;

import com.arassec.jptp.core.datatype.valuerange.ContainerType;
import com.arassec.jptp.core.datatype.valuerange.OperationCode;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.variable.DataContainerPayload;
import com.arassec.jptp.core.datatype.variable.TransactionId;

import java.nio.ByteBuffer;

public record DataContainer<P extends DataContainerPayload<P>>(
        UnsignedInt length,
        ContainerType containerType,
        OperationCode operationCode,
        TransactionId transactionId,
        P payload) {

    public static <P extends DataContainerPayload<P>> DataContainer<P> deserialize(ByteBuffer buffer, P payloadInstance) {
        return new DataContainer<>(
                UnsignedInt.deserialize(buffer),
                ContainerType.valueOf(UnsignedShort.deserialize(buffer)),
                OperationCode.valueOf(UnsignedShort.deserialize(buffer)),
                new TransactionId(UnsignedInt.deserialize(buffer)),
                payloadInstance.deserialize(buffer)
        );
    }

}
