package com.arassec.jptp.core.container;

import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.valuerange.ContainerType;
import com.arassec.jptp.core.datatype.valuerange.ResponseCode;
import com.arassec.jptp.core.datatype.variable.SessionId;
import com.arassec.jptp.core.datatype.variable.TransactionId;

import java.nio.ByteBuffer;

/**
 * A response container which is received after sending a command to a PTP device.
 *
 * @param length        The container's total length.
 * @param containerType The container type of the command which this response container belongs to.
 * @param responseCode  A PTP response code.
 * @param transactionId The PTP transaction ID used in the command.
 * @param sessionId     The PTP session ID.
 */
public record ResponseContainer(
        UnsignedInt length,
        ContainerType containerType,
        ResponseCode responseCode,
        TransactionId transactionId,
        SessionId sessionId) {

    /**
     * Deserializes the supplied byte buffer into a response container.
     *
     * @param buffer The {@link ByteBuffer} containing the response container's data.
     * @return A new {@link ResponseContainer} instance.
     */
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
