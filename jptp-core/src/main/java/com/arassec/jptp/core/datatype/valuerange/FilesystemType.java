package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;

public record FilesystemType(UnsignedShort code, String description) {

    public static final FilesystemType UNDEFINED = new FilesystemType(UnsignedShort.valueOf((short) 0x0000), "Undefined");
    public static final FilesystemType GENERIC_FLAT = new FilesystemType(UnsignedShort.valueOf((short) 0x0001), "Generic flat");
    public static final FilesystemType GENERIC_HIERARCHICAL = new FilesystemType(UnsignedShort.valueOf((short) 0x0002), "Generic hierarchical");
    public static final FilesystemType DCF = new FilesystemType(UnsignedShort.valueOf((short) 0x0003), "DCF");

    public static FilesystemType valueOf(UnsignedShort code) {
        return switch (code.value()) {
            case 0x0000 -> UNDEFINED;
            case 0x0001 -> GENERIC_FLAT;
            case 0x0002 -> GENERIC_HIERARCHICAL;
            case 0x0003 -> DCF;
            default -> new FilesystemType(code, "TODO");
        };
    }

}
