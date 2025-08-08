package com.arassec.jptp.core.container;

import com.arassec.jptp.core.PayloadDeserializer;
import com.arassec.jptp.core.datatype.ContainerType;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.complex.OperationCode;
import com.arassec.jptp.core.datatype.complex.TransactionId;

import java.nio.ByteBuffer;

/**
 * A data container, which might be received as part of the response to a PTP command execution.
 *
 * @param length        The container's total length.
 * @param containerType The container type.
 * @param operationCode The original operation code which this data is related to.
 * @param transactionId The PTP transaction ID used in the original command.
 * @param payload       The actual payload of the data container.
 * @param <P>           The type of the received payload.
 */
public record DataContainer<P>(
        UnsignedInt length,
        ContainerType containerType,
        OperationCode operationCode,
        TransactionId transactionId,
        P payload) {

    /**
     * Deserializes the supplied byte buffer into a data container instance.
     *
     * @param buffer              The {@link ByteBuffer} containing the data container.
     * @param payloadDeserializer A deserializer for the container's payload.
     * @param <P>                 The type of the expected payload.
     * @return A new {@link DataContainer} instance containing the deserialized data.
     */
    public static <P> DataContainer<P> deserialize(ByteBuffer buffer, PayloadDeserializer<P> payloadDeserializer) {
        return new DataContainer<>(
                UnsignedInt.deserialize(buffer),
                ContainerType.valueOf(UnsignedShort.deserialize(buffer)),
                OperationCode.valueOf(UnsignedShort.deserialize(buffer)),
                new TransactionId(UnsignedInt.deserialize(buffer)),
                payloadDeserializer.deserialize(buffer)
        );
    }

}
