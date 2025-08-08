package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link EventCode}.
 */
class EventCodeTest extends ValueRangeTest {

    /**
     * Tests the string representation.
     */
    @Test
    void testToString() {
        assertThat(EventCode.DEVICE_RESET)
                .hasToString("EventCode[code='0x400B', name='DeviceReset']");
    }

    /**
     * Tests deserialization of a PTP array.
     */
    @Test
    void testDeserialize() {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(2); // Array length
        buffer.putShort(EventCode.DEVICE_PROP_CHANGED.code().value());
        buffer.putShort(EventCode.CAPTURE_COMPLETE.code().value());
        buffer.rewind();

        List<EventCode> devicePropCodes = EventCode.deserializeArray(buffer);
        assertThat(devicePropCodes).hasSize(2);
        assertThat(devicePropCodes.get(0)).isEqualTo(EventCode.DEVICE_PROP_CHANGED);
        assertThat(devicePropCodes.get(1)).isEqualTo(EventCode.CAPTURE_COMPLETE);
    }

    /**
     * Tests the record's value range.
     */
    @Test
    void testValueRange() {
        testValueRange(EventCode.class, object -> ((EventCode) object).code());
    }

}
