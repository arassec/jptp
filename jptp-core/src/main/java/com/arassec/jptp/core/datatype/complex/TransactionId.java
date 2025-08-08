package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.datatype.UnsignedInt;

import javax.annotation.processing.Generated;

/**
 * The 'Transaction ID' data type.
 *
 * @param id The transaction ID as UINT32.
 */
@Generated("human")
public record TransactionId(UnsignedInt id) {
}
