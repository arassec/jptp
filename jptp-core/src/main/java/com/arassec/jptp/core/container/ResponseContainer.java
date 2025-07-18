package com.arassec.jptp.core.container;

import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.valuerange.ContainerType;
import com.arassec.jptp.core.datatype.valuerange.ResponseCode;
import com.arassec.jptp.core.datatype.variable.SessionId;
import com.arassec.jptp.core.datatype.variable.TransactionId;

import java.nio.ByteBuffer;

public record ResponseContainer(
        UnsignedInt length,
        ContainerType containerType,
        ResponseCode responseCode,
        TransactionId transactionId,
        SessionId sessionId) {

    public static ResponseContainer deserialize(ByteBuffer buffer) {
        return new ResponseContainer(
                UnsignedInt.deserialize(buffer),
                ContainerType.valueOf(UnsignedShort.deserialize(buffer)),
                ResponseCode.valueOf(UnsignedShort.deserialize(buffer)),
                new TransactionId(UnsignedInt.deserialize(buffer)),
                new SessionId(UnsignedInt.deserialize(buffer))
        );
    }

}
