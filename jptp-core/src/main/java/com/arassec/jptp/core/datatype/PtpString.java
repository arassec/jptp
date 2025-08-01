package com.arassec.jptp.core.datatype;

import java.nio.ByteBuffer;

/**
 * Value object for a PTP string.
 *
 * @param value The string's value.
 */
public record PtpString(String value) {

    /**
     * Deserializes a PTP string into this object.
     *
     * @param buffer The {@link ByteBuffer} containing the string.
     * @return A new {@link PtpString} instance containing the string value.
     */
    public static PtpString deserialize(ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return new PtpString("");
        }

        byte stringLength = buffer.get();
        if (stringLength <= 0) {
            return new PtpString("");
        }

        StringBuilder stringBuilder = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            if (buffer.remaining() >= 2) {
                stringBuilder.append(buffer.getChar());
            } else if (buffer.hasRemaining()) {

                stringBuilder.append(buffer.get());
            }
        }

        // Remove null chars at the end!
        return new PtpString(stringBuilder.toString().replace("\u0000", ""));
    }

}
