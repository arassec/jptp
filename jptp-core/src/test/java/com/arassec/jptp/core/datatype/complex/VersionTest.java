package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Version}.
 */
class VersionTest extends ValueRangeTest {

    /**
     * Tests the record's value range.
     */
    @Test
    void testValueRange() {
        testValueRange(Version.class, object -> ((Version) object).version());
    }

}
