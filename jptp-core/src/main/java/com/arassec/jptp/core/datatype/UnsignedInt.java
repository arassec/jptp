package com.arassec.jptp.core.datatype;

import java.nio.ByteBuffer;

public record UnsignedInt(int value) {

    public static UnsignedInt nullInstance() {
        return new UnsignedInt(nullValue());
    }

    public static int nullValue() {
        return 0x00000000;
    }

    public static UnsignedInt valueOf(int value) {
        return new UnsignedInt(value);
    }

    public static UnsignedInt deserialize(ByteBuffer buffer) {
        return new UnsignedInt(buffer.getInt());
    }

}
