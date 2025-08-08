package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link ObjectHandleArray}.
 */
class ObjectHandleArrayTest {

    /**
     * Tests the deserializer.
     */
    @Test
    void testDeserialize() {
        ByteBuffer buffer = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(2);
        buffer.putInt(3);
        buffer.putInt(4);
        buffer.rewind();

        ObjectHandleArray objectHandleArray = ObjectHandleArray.DESERIALIZER.deserialize(buffer);

        assertThat(objectHandleArray.handles()).hasSize(2);
        assertThat(objectHandleArray.handles().get(0).handle().value()).isEqualTo(3);
        assertThat(objectHandleArray.handles().get(1).handle().value()).isEqualTo(4);
    }

}
