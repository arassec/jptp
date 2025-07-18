package com.arassec.jptp.core.datatype;

import java.nio.ByteBuffer;

public record UnsignedLong(long value) {

    public static final UnsignedLong FIELD_UNUSED = new UnsignedLong(0xFFFFFFFFL);

    public static UnsignedLong valueOf(int value) {
        return new UnsignedLong(value);
    }

    public static UnsignedLong deserialize(ByteBuffer buffer) {
        return new UnsignedLong(buffer.getLong());
    }

}
