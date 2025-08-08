package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.PayloadDeserializer;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

/**
 * The 'data object' data type.
 *
 * @param data The data as byte array.
 */
public record DataObject(byte[] data) {

    /**
     * {@link PayloadDeserializer} for this record.
     */
    public static final PayloadDeserializer<DataObject> DESERIALIZER = buffer -> {
        ByteBuffer slicedBuffer = buffer.slice();
        byte[] bytes = new byte[slicedBuffer.remaining()];
        slicedBuffer.get(bytes);
        return new DataObject(bytes);
    };

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
