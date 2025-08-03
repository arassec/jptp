package com.arassec.jptp.core.datatype.payload;

import com.arassec.jptp.core.PtpContainerPayload;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DataObject that = (DataObject) o;
        return Objects.deepEquals(data, that.data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
