package com.arassec.jptp.core;

import java.nio.ByteBuffer;

/**
 * Defines data types that are used in PTP data containers as payload.
 *
 * @param <P> The type of the actual container payload.
 */
public interface PtpContainerPayload<P> {

    /**
     * Deserializes the container's payload from the supplied byte buffer.
     *
     * @param buffer The {@link ByteBuffer} of the container, positioned at the payload's starting byte.
     * @return The deserialized payload.
     */
    P deserialize(ByteBuffer buffer);

}
