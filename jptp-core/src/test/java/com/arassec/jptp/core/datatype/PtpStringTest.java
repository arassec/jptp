package com.arassec.jptp.core.datatype;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link PtpString} datatype.
 */
class PtpStringTest {

    /**
     * Tests deserialization.
     */
    @Test
    void testDeserialize() {
        byte[] bytes = "jptp PtpStringTest".getBytes(StandardCharsets.UTF_16LE);

        ByteBuffer buffer = ByteBuffer.allocate(bytes.length + 3)
                .order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) bytes.length);
        buffer.put(bytes);
        buffer.put("\u0000".getBytes(StandardCharsets.UTF_16LE));
        buffer.rewind();

        PtpString ptpString = PtpString.deserialize(buffer);

        assertThat(ptpString.value()).isEqualTo("jptp PtpStringTest");
    }

}
