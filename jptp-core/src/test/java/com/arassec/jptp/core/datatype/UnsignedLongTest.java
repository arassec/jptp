package com.arassec.jptp.core.datatype;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link UnsignedLong} datatype.
 */
class UnsignedLongTest {

    /**
     * Tests instance creation.
     */
    @Test
    void testInstanceCreation() {
        assertThat(UnsignedLong.FIELD_UNUSED.value())
                .isEqualTo(UnsignedLong.valueOf(4294967295L).value());
    }

    /**
     * Tests deserialization.
     */
    @Test
    void testDeserialize() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(4711L);
        buffer.rewind();

        assertThat(UnsignedLong.deserialize(buffer).value()).isEqualTo(4711L);
    }

}
