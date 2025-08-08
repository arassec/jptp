package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link DevicePropCode} implementation.
 */
class DevicePropCodeTest extends ValueRangeTest {

    /**
     * Tests the string representation.
     */
    @Test
    void testToString() {
        assertThat(DevicePropCode.AUDIO_BITRATE)
                .hasToString("DevicePropCode[code='0x5029', name='AudioBitrate']");
    }

    /**
     * Tests deserialization of a PTP array.
     */
    @Test
    void testDeserialize() {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(2); // Array length
        buffer.putShort(DevicePropCode.BATTERY_LEVEL.code().value());
        buffer.putShort(DevicePropCode.FLASH_MODE.code().value());
        buffer.rewind();

        List<DevicePropCode> devicePropCodes = DevicePropCode.deserializeArray(buffer);
        assertThat(devicePropCodes).hasSize(2);
        assertThat(devicePropCodes.get(0)).isEqualTo(DevicePropCode.BATTERY_LEVEL);
        assertThat(devicePropCodes.get(1)).isEqualTo(DevicePropCode.FLASH_MODE);
    }

    /**
     * Tests the record's value range.
     */
    @Test
    void testValueRange() {
        testValueRange(DevicePropCode.class, object -> ((DevicePropCode) object).code());
    }

}
