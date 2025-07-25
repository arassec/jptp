package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.datatype.UnsignedInt;

/**
 * Returns the 'Free space in images' data type.
 *
 * @param freeSpace The free space in number of images as UINT32.
 */
public record FreeSpaceInImages(UnsignedInt freeSpace) {
}
