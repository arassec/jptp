package com.arassec.jptp.core.datatype.payload;

import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.datatype.UnsignedInt;

import java.nio.ByteBuffer;

/**
 * The 'Object handle' data type.
 *
 * @param handle The object handle as UINT32.
 */
public record ObjectHandle(UnsignedInt handle) implements PtpContainerPayload<ObjectHandle> {

    /**
     * Creates an empty instance.
     *
     * @return A new {@link ObjectHandle} without data.
     */
    public static ObjectHandle emptyInstance() {
        return new ObjectHandle(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectHandle deserialize(ByteBuffer buffer) {
        return new ObjectHandle(UnsignedInt.deserialize(buffer));
    }

}
