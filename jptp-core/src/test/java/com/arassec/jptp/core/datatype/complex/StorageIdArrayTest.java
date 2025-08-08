package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link ObjectHandleArray}.
 */
class StorageIdArrayTest {

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

        StorageIdArray storageIdArray = StorageIdArray.DESERIALIZER.deserialize(buffer);

        assertThat(storageIdArray.ids()).hasSize(2);
        assertThat(storageIdArray.ids().get(0).id().value()).isEqualTo(3);
        assertThat(storageIdArray.ids().get(1).id().value()).isEqualTo(4);
    }

}
