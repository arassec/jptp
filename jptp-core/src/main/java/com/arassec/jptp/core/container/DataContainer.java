package com.arassec.jptp.core.container;

import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.valuerange.ContainerType;
import com.arassec.jptp.core.datatype.valuerange.OperationCode;
import com.arassec.jptp.core.datatype.simple.TransactionId;

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
public record DataContainer<P extends PtpContainerPayload<P>>(
        UnsignedInt length,
        ContainerType containerType,
        OperationCode operationCode,
        TransactionId transactionId,
        P payload) {

    /**
     * Deserializes the supplied byte buffer into a data container instance.
     *
     * @param buffer          The {@link ByteBuffer} containing the data container.
     * @param payloadInstance An instance of the expected payload. Used to deserialize it.
     * @param <P>             The type of the expected payload.
     * @return A new {@link DataContainer} instance containing the deserialized data.
     */
    public static <P extends PtpContainerPayload<P>> DataContainer<P> deserialize(ByteBuffer buffer, P payloadInstance) {
        return new DataContainer<>(
                UnsignedInt.deserialize(buffer),
                ContainerType.valueOf(UnsignedShort.deserialize(buffer)),
                OperationCode.valueOf(UnsignedShort.deserialize(buffer)),
                new TransactionId(UnsignedInt.deserialize(buffer)),
                payloadInstance.deserialize(buffer)
        );
    }

}
