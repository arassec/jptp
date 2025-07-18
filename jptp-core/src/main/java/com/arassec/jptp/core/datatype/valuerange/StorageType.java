package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;

public record StorageType(UnsignedShort code, String type) {

    public static final StorageType UNDEFINED = new StorageType(UnsignedShort.valueOf((short) 0x0000), "Undefined");
    public static final StorageType FIXED_ROM = new StorageType(UnsignedShort.valueOf((short) 0x0001), "Fixed ROM");
    public static final StorageType REMOVABLE_ROM = new StorageType(UnsignedShort.valueOf((short) 0x0002), "Removable ROM");
    public static final StorageType FIXED_RAM = new StorageType(UnsignedShort.valueOf((short) 0x0003), "Fixed RAM");
    public static final StorageType REMOVABLE_RAM = new StorageType(UnsignedShort.valueOf((short) 0x0004), "Removable RAM");

    public static StorageType valueOf(UnsignedShort code) {
        return switch (code.value()) {
            case 0x0000 -> UNDEFINED;
            case 0x0001 -> FIXED_ROM;
            case 0x0002 -> REMOVABLE_ROM;
            case 0x0003 -> FIXED_RAM;
            case 0x0004 -> REMOVABLE_RAM;
            default -> new StorageType(code, "Reserved");
        };
    }

}
