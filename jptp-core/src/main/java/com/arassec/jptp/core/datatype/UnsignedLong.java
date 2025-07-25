package com.arassec.jptp.core.datatype;

import java.nio.ByteBuffer;

/**
 * Value object containing a PTP UINT64 value.
 *
 * @param value The UINT64 value as long.
 */
public record UnsignedLong(long value) {

    /**
     * Constant denoting an unused PTP field.
     */
    public static final UnsignedLong FIELD_UNUSED = new UnsignedLong(0xFFFFFFFFL);

    /**
     * Returns a new instance containing the supplied value.
     *
     * @param value The value to use as UINT64.
     * @return A new instance containing the supplied value.
     */
    public static UnsignedLong valueOf(long value) {
        return new UnsignedLong(value);
    }

    /**
     * Deserializes the supplied byte buffer into a new instance.
     *
     * @param buffer The {@link ByteBuffer} containing the UINT64 value.
     * @return A new instance containing the value.
     */
    public static UnsignedLong deserialize(ByteBuffer buffer) {
        return new UnsignedLong(buffer.getLong());
    }

}
