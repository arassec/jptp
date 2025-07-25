package com.arassec.jptp.core.datatype;

import java.nio.ByteBuffer;

/**
 * Value object containing a PTP UINT16 value as short.
 *
 * @param value The value as short.
 */
public record UnsignedShort(short value) {

    /**
     * Creates a new instance with the supplied value.
     *
     * @param value The UINT16 value to use.
     * @return A new instance with the value.
     */
    public static UnsignedShort valueOf(short value) {
        return new UnsignedShort(value);
    }

    /**
     * Deserializes the supplied byte buffer into a new instance.
     *
     * @param buffer The {@link ByteBuffer} containing the UINT16.
     * @return A new instance containing the value.
     */
    public static UnsignedShort deserialize(ByteBuffer buffer) {
        return new UnsignedShort(buffer.getShort());
    }

}
