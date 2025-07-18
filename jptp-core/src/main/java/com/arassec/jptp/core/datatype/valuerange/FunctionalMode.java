package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;

public record FunctionalMode(UnsignedShort mode, String description) {

    public static FunctionalMode STANDARD = new FunctionalMode(UnsignedShort.valueOf((short) 0x0000), "Standard mode");
    public static FunctionalMode SLEEP = new FunctionalMode(UnsignedShort.valueOf((short) 0x0001), "Sleep mode");

    public static FunctionalMode valueOf(UnsignedShort mode) {
        return switch (mode.value()) {
            case 0x0000 -> STANDARD;
            case 0x0001 -> SLEEP;
            default -> new FunctionalMode(mode, "TODO");
        };
    }

}
