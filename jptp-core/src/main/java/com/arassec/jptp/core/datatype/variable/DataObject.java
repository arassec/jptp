package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.PtpContainerPayload;

import java.nio.ByteBuffer;

/**
 * The 'data object' data type.
 *
 * @param data The data as byte array.
 */
public record DataObject(byte[] data) implements PtpContainerPayload<DataObject> {

    /**
     * An empty instance for deserialization purposes.
     */
    public static final DataObject emptyInstance = new DataObject(new byte[0]);

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObject deserialize(ByteBuffer buffer) {
        ByteBuffer slicedBuffer = buffer.slice();
        byte[] bytes = new byte[slicedBuffer.remaining()];
        slicedBuffer.get(bytes);
        return new DataObject(bytes);
    }

}
