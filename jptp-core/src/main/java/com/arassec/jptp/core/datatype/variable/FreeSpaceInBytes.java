package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.datatype.UnsignedLong;

public record FreeSpaceInBytes(UnsignedLong freeSpace) {

    public boolean isSet() {
        return !UnsignedLong.FIELD_UNUSED.equals(freeSpace);
    }

}
