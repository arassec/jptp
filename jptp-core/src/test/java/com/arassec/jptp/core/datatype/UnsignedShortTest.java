package com.arassec.jptp.core.datatype;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link UnsignedShort} datatype.
 */
class UnsignedShortTest {

    /**
     * Tests deserialization.
     */
    @Test
    void testDeserialize() {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort((short) 23);
        buffer.rewind();

        assertThat(UnsignedShort.deserialize(buffer)).isEqualTo(UnsignedShort.valueOf((short) 23));
    }

}
