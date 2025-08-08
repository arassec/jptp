package com.arassec.jptp.core.datatype.complex;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link AssociationCode}.
 */
class AssociationCodeTest extends ValueRangeTest {

    /**
     * Tests the string representation.
     */
    @Test
    void testToString() {
        assertThat(AssociationCode.ALBUM.toString())
                .isEqualTo("AssociationCode[code='0x0002', type='Album']");
    }

    /**
     * Tests the record's value range.
     */
    @Test
    void testValueRange() {
        testValueRange(AssociationCode.class, object -> ((AssociationCode) object).code());
    }

}
