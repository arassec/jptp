package com.arassec.jptp.core.datatype;

import java.nio.ByteBuffer;

public record PtpString(String rawValue) {

    public static PtpString deserialize(ByteBuffer buffer) {
        byte stringLength = buffer.get();

        if (stringLength <= 0) {
            return new PtpString("");
        }

        StringBuilder stringBuilder = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            stringBuilder.append(buffer.getChar());
        }

        // Remove null chars at the end!
        return new PtpString(stringBuilder.toString().replace("\u0000", ""));
    }

}
