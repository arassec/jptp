package com.arassec.jptp.core.container;

import com.arassec.jptp.core.PayloadDeserializer;
import com.arassec.jptp.core.datatype.ContainerType;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.complex.EventCode;
import com.arassec.jptp.core.datatype.complex.TransactionId;

import java.nio.ByteBuffer;

/**
 * A PTP event container that contains event data.
 *
 * @param length        The container's total length.
 * @param containerType The container's type.
 * @param eventCode     The PTP event code.
 * @param transactionId The PTP transaction ID.
 * @param payload       The payload of the event.
 * @param <P>           The type of the event's payload.
 */
public record EventContainer<P>(
        UnsignedInt length,
        ContainerType containerType,
        EventCode eventCode,
        TransactionId transactionId,
        P payload) {

    /**
     * Deserializes the event container from the supplied byte buffer.
     *
     * @param buffer              The {@link ByteBuffer} containing the event container.
     * @param payloadDeserializer A deserializer for the event's payload.
     * @param <P>                 The type of the payload.
     * @return A new {@link EventContainer} instance containing all data from the byte buffer.
     */
    public static <P> EventContainer<P> deserialize(ByteBuffer buffer, PayloadDeserializer<P> payloadDeserializer) {
        return new EventContainer<>(
                UnsignedInt.deserialize(buffer),
                ContainerType.valueOf(UnsignedShort.deserialize(buffer)),
                EventCode.valueOf(UnsignedShort.deserialize(buffer)),
                new TransactionId(UnsignedInt.deserialize(buffer)),
                payloadDeserializer.deserialize(buffer)
        );
    }

}
