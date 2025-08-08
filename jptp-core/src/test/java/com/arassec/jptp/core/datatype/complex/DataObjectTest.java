package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link DataObject}.
 */
class DataObjectTest {

    /**
     * Tests equals and hashcode of data objects.
     */
    @Test
    void testEqualsAndHashCode() {
        byte[] data = {1, 2, 3, 4};

        DataObject dataObject = new DataObject(data);

        assertThat(dataObject.equals(new DataObject(data))).isTrue();
        assertThat(dataObject.hashCode()).hasSameHashCodeAs(new DataObject(data));
    }

    /**
     * Tests deserializing a data object.
     */
    @Test
    void testDeserialize() {
        byte[] data = {1, 2, 3, 4};

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.get(); // Read first buffer entry.

        DataObject dataObject = DataObject.DESERIALIZER.deserialize(buffer);

        byte[] expectedData = {2, 3, 4};
        assertThat(dataObject.data()).isEqualTo(expectedData);
    }

}
