package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.datatype.UnsignedShort;

/**
 * Value object modelling a PTP filesystem type.
 *
 * @param code        The filesystem type's code.
 * @param description A description.
 */
public record FilesystemType(UnsignedShort code, String description) {

    public static final FilesystemType UNDEFINED = new FilesystemType(UnsignedShort.valueOf((short) 0x0000), "Undefined");
    public static final FilesystemType GENERIC_FLAT = new FilesystemType(UnsignedShort.valueOf((short) 0x0001), "Generic flat");
    public static final FilesystemType GENERIC_HIERARCHICAL = new FilesystemType(UnsignedShort.valueOf((short) 0x0002), "Generic hierarchical");
    public static final FilesystemType DCF = new FilesystemType(UnsignedShort.valueOf((short) 0x0003), "DCF");

    /**
     * Returns an instance for the given code.
     *
     * @param code The code to use.
     * @return A {@link FilesystemType} for the given code.
     */
    public static FilesystemType valueOf(UnsignedShort code) {
        return switch (code.value()) {
            case 0x0000 -> UNDEFINED;
            case 0x0001 -> GENERIC_FLAT;
            case 0x0002 -> GENERIC_HIERARCHICAL;
            case 0x0003 -> DCF;
            default -> new FilesystemType(code, "Reserved / Vendor-defined");        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "FilesystemType[code='" + String.format("0x%04X", code.value()) + "', description='" + description + "']";
    }

}
