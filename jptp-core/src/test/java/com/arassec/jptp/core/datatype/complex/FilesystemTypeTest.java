package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link FilesystemType}.
 */
class FilesystemTypeTest extends ValueRangeTest {

    /**
     * Tests the string representation.
     */
    @Test
    void testToString() {
        assertThat(FilesystemType.GENERIC_FLAT)
                .hasToString("FilesystemType[code='0x0001', description='Generic flat']");
    }

    /**
     * Tests the record's value range.
     */
    @Test
    void testValueRange() {
        testValueRange(FilesystemType.class, object -> ((FilesystemType) object).code());
    }

}
