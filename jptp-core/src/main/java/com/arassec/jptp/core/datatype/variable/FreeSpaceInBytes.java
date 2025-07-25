package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.datatype.UnsignedLong;

/**
 * The 'Free space in bytes' data type.
 *
 * @param freeSpace The free space as UINT64.
 */
public record FreeSpaceInBytes(UnsignedLong freeSpace) {
}
