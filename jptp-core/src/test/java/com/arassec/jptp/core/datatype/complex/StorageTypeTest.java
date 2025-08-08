package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link StorageType}.
 */
class StorageTypeTest extends ValueRangeTest {

    /**
     * Tests the string representation.
     */
    @Test
    void testToString() {
        assertThat(StorageType.REMOVABLE_RAM.toString())
                .isEqualTo("StorageType[code='0x0004', type='Removable RAM']");
    }

    /**
     * Tests the record's value range.
     */
    @Test
    void testValueRange() {
        testValueRange(StorageType.class, object -> ((StorageType) object).code());
    }

}
