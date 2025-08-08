package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link FunctionalMode}.
 */
class FunctionalModeTest extends ValueRangeTest {

    /**
     * Tests the string representation.
     */
    @Test
    void testToString() {
        assertThat(FunctionalMode.STANDARD.toString())
                .isEqualTo("FunctionalMode[mode='0x0000', description='Standard mode']");
    }

    /**
     * Tests the record's value range.
     */
    @Test
    void testValueRange() {
        testValueRange(FunctionalMode.class, object -> ((FunctionalMode) object).mode());
    }

}
