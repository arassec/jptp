package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;

public record ProtectionStatus(UnsignedShort code, String description) {

    public static final ProtectionStatus NO_PROTECTION = new ProtectionStatus(UnsignedShort.valueOf((short) 0x0000), "No protection");
    public static final ProtectionStatus READ_ONLY = new ProtectionStatus(UnsignedShort.valueOf((short) 0x0001), "Read only");

    public static ProtectionStatus valueOf(UnsignedShort code) {
        return switch (code.value()) {
            case 0x0000 -> NO_PROTECTION;
            case 0x0001 -> READ_ONLY;
            default -> new ProtectionStatus(code, "Reserved");
        };
    }

}
