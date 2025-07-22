package com.arassec.jptp.core.container;

import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.valuerange.ContainerType;
import com.arassec.jptp.core.datatype.valuerange.EventCode;
import com.arassec.jptp.core.datatype.variable.TransactionId;

import java.nio.ByteBuffer;

public record EventContainer<P extends PtpContainerPayload<P>>(
        UnsignedInt length,
        ContainerType containerType,
        EventCode eventCode,
        TransactionId transactionId,
        P payload) {

    public static <P extends PtpContainerPayload<P>> EventContainer<P> deserialize(ByteBuffer buffer, P payloadInstance) {
        return new EventContainer<>(
                UnsignedInt.deserialize(buffer),
                ContainerType.valueOf(UnsignedShort.deserialize(buffer)),
                EventCode.valueOf(UnsignedShort.deserialize(buffer)),
                new TransactionId(UnsignedInt.deserialize(buffer)),
                payloadInstance.deserialize(buffer)
        );
    }

}
