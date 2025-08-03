package com.arassec.jptp.core.datatype.simple;

import com.arassec.jptp.core.datatype.UnsignedInt;

/**
 * The 'Sequence number' data type.
 *
 * @param number The sequence number as UINT32.
 */
public record SequenceNumber(UnsignedInt number) {
}
