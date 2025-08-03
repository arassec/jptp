package com.arassec.jptp.core.datatype;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link UnsignedInt} datatype.
 */
class UnsignedIntTest {

    /**
     * Tests instance creation and conversion.
     */
    @Test
    void testInstanceCreationAndConversion() {
        assertThat(UnsignedInt.valueOf(0).value()).isEqualTo(UnsignedInt.zero());
        assertThat(UnsignedInt.zeroInstance().value()).isEqualTo(UnsignedInt.zero());

        assertThat(UnsignedInt.max()).isEqualTo(-1);
        assertThat(UnsignedInt.maxInstance().value()).isEqualTo(-1);
    }

    /**
     * Tests deserialization.
     */
    @Test
    void testDeserialize() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(42);
        buffer.rewind();

        assertThat(UnsignedInt.deserialize(buffer).value()).isEqualTo(42);
    }

}
