package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.PtpContainerPayload;

import java.nio.ByteBuffer;

/**
 * Unspecified in the PTP standard, this helper object can be used as return value if no data container is received
 * after an operation.
 */
public record NoData() implements PtpContainerPayload<NoData> {

    public static NoData emptyInstance() {
        return new NoData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoData deserialize(ByteBuffer buffer) {
        return new NoData();
    }

}
