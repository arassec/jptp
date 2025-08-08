package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link ResponseCode}.
 */
class ResponseCodeTest extends ValueRangeTest {

    /**
     * Tests the string representation.
     */
    @Test
    void testToString() {
        assertThat(ResponseCode.INVALID_CODE_FORMAT.toString())
                .isEqualTo("ResponseCode[code='0x2016', description='Invalid Code Format']");
    }

    /**
     * Tests the record's value range.
     */
    @Test
    void testValueRange() {
        testValueRange(ResponseCode.class, object -> ((ResponseCode) object).code());
    }

}
