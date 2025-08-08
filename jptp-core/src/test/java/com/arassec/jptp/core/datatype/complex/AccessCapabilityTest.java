package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link AccessCapability}.
 */
class AccessCapabilityTest extends ValueRangeTest {

    /**
     * Tests the string representation.
     */
    @Test
    void testToString() {
        assertThat(AccessCapability.READ_ONLY_WITHOUT_OBJECT_DELETION)
                .hasToString("AccessCapability[code='0x0001', description='Read-only without object deletion']");
    }

    /**
     * Tests the record's value range.
     */
    @Test
    void testValueRange() {
        testValueRange(AccessCapability.class, object -> ((AccessCapability) object).code());
    }

}
