package com.arassec.jptp.core.datatype;

import java.nio.ByteBuffer;

/**
 * Value object modelling a PTP UINT32 value as int.
 *
 * @param value The UINT32 value.
 */
public record UnsignedInt(int value) {

    /**
     * Creates a new instance with only null values.
     *
     * @return A new instance.
     */
    public static UnsignedInt nullInstance() {
        return new UnsignedInt(nullValue());
    }

    /**
     * The PTP null value as int.
     *
     * @return 0
     */
    public static int nullValue() {
        return 0x00000000;
    }

    /**
     * Returns a new instance containing the supplied value.
     *
     * @param value The value to use.
     * @return A new instance containing the supplied value.
     */
    public static UnsignedInt valueOf(int value) {
        return new UnsignedInt(value);
    }

    /**
     * Deserializes the supplied byte buffer into a new instance.
     *
     * @param buffer The {@link ByteBuffer} containing an UINT32 value.
     * @return A new {@link UnsignedInt} instance with the supplied value.
     */
    public static UnsignedInt deserialize(ByteBuffer buffer) {
        return new UnsignedInt(buffer.getInt());
    }

}
