package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.datatype.UnsignedInt;

/**
 * The 'Object compressed size' data type.
 *
 * @param size The size in bytes as UINT32.
 */
public record ObjectCompressedSize(UnsignedInt size) {
}
