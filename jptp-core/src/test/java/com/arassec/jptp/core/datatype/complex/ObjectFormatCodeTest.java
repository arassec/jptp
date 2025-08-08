package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link ObjectFormatCode}.
 */
class ObjectFormatCodeTest extends ValueRangeTest {

    /**
     * Tests the string representation.
     */
    @Test
    void testToString() {
        assertThat(ObjectFormatCode.EXIF_JPEG.toString())
                .isEqualTo("ObjectFormatCode[code='0x3801', format='Exif/JPEG']");
    }

    /**
     * Tests deserialization of a PTP array.
     */
    @Test
    void testDeserialize() {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(2); // Array length
        buffer.putShort(ObjectFormatCode.EXIF_JPEG.code().value());
        buffer.putShort(ObjectFormatCode.MP3.code().value());
        buffer.rewind();

        List<ObjectFormatCode> devicePropCodes = ObjectFormatCode.deserializeArray(buffer);
        assertThat(devicePropCodes.size()).isEqualTo(2);
        assertThat(devicePropCodes.get(0)).isEqualTo(ObjectFormatCode.EXIF_JPEG);
        assertThat(devicePropCodes.get(1)).isEqualTo(ObjectFormatCode.MP3);
    }

    /**
     * Tests the record's value range.
     */
    @Test
    void testValueRange() {
        testValueRange(ObjectFormatCode.class, object -> ((ObjectFormatCode) object).code());
    }

}
