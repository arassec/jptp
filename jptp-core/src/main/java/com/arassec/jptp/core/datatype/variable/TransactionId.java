package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.datatype.UnsignedInt;

/**
 * The 'Transaction ID' data type.
 *
 * @param id The transaction ID as UINT32.
 */
public record TransactionId(UnsignedInt id) {
}
