package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link ProtectionStatus}.
 */
class ProtectionStatusTest extends ValueRangeTest {

    /**
     * Tests the string representation.
     */
    @Test
    void testToString() {
        assertThat(ProtectionStatus.READ_ONLY.toString())
                .isEqualTo("ProtectionStatus[code='0x0001', description='Read only']");
    }

    /**
     * Tests the record's value range.
     */
    @Test
    void testValueRange() {
        testValueRange(ProtectionStatus.class, object -> ((ProtectionStatus) object).code());
    }

}
