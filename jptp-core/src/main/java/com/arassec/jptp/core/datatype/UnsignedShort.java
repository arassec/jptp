package com.arassec.jptp.core.datatype;

import java.nio.ByteBuffer;

/**
 * PTP data format UINT16
 */
public record UnsignedShort(short value) {

    public static UnsignedShort valueOf(short value) {
        return new UnsignedShort(value);
    }

    public static UnsignedShort deserialize(ByteBuffer buffer) {
        return new UnsignedShort(buffer.getShort());
    }

}
