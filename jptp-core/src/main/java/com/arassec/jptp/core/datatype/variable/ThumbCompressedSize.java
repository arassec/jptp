package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.datatype.UnsignedInt;

/**
 * The 'Thumb compressed size' data type.
 *
 * @param size The compressed thumbnail's size in bytes as UINT32.
 */
public record ThumbCompressedSize(UnsignedInt size) {
}
