package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.datatype.UnsignedShort;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link OperationCode}.
 */
class OperationCodeTest extends ValueRangeTest {

    /**
     * Tests equals and hashCode.
     */
    @Test
    void testEqualsAndHashCode() {
        var op1 = new OperationCode(UnsignedShort.valueOf((short) 0x1001), "Name1", Version.V1_0);
        var op2 = new OperationCode(UnsignedShort.valueOf((short) 0x1001), "Name2", Version.V1_1);
        var op3 = new OperationCode(UnsignedShort.valueOf((short) 0x1002), "Name1", Version.V1_0);

        assertThat(op1).isEqualTo(op2)
                .hasSameHashCodeAs(op2)
                .isNotEqualTo(op3)
                .isNotEqualTo(null)
                .isNotEqualTo(new Object());
    }

    /**
     * Tests the string representation.
     */
    @Test
    void testToString() {
        assertThat(OperationCode.GET_DEVICE_INFO)
                .hasToString("OperationCode[code='0x1001', name='GetDeviceInfo']");
    }

    /**
     * Tests deserialization of a PTP array.
     */
    @Test
    void testDeserialize() {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(2); // Array length
        buffer.putShort(OperationCode.INITIATE_CAPTURE.code().value());
        buffer.putShort(OperationCode.GET_RESIZED_IMAGE_OBJECT.code().value());
        buffer.rewind();

        List<OperationCode> devicePropCodes = OperationCode.deserializeArray(buffer);
        assertThat(devicePropCodes).hasSize(2);
        assertThat(devicePropCodes.get(0)).isEqualTo(OperationCode.INITIATE_CAPTURE);
        assertThat(devicePropCodes.get(1)).isEqualTo(OperationCode.GET_RESIZED_IMAGE_OBJECT);
    }

    /**
     * Tests the record's value range.
     */
    @Test
    void testValueRange() {
        testValueRange(OperationCode.class, object -> ((OperationCode) object).code());
    }

}
