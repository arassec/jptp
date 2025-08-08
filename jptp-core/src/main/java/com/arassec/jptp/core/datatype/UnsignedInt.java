package com.arassec.jptp.core.datatype;

import java.nio.ByteBuffer;

/**
 * Value object modelling a PTP UINT32 value as int.
 *
 * @param value The UINT32 value.
 */
public record UnsignedInt(int value) {

    /**
     * Zero value.
     */
    public static final int ZERO = 0x00000000;

    /**
     * Max value.
     */
    public static final int MAX = 0xFFFFFFFF;

    /**
     * Creates a new instance with a null value.
     *
     * @return A new instance.
     */
    public static UnsignedInt zeroInstance() {
        return new UnsignedInt(ZERO);
    }

    /**
     * Creates a new instance with the maximum value.
     *
     * @return A new instance.
     */
    public static UnsignedInt maxInstance() {
        return new UnsignedInt(MAX);
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
