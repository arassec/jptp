package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;

/**
 * Value object modeling a PTP protection status.
 *
 * @param code        The protection status' code.
 * @param description A description.
 */
public record ProtectionStatus(UnsignedShort code, String description) {

    public static final ProtectionStatus NO_PROTECTION = new ProtectionStatus(UnsignedShort.valueOf((short) 0x0000), "No protection");
    public static final ProtectionStatus READ_ONLY = new ProtectionStatus(UnsignedShort.valueOf((short) 0x0001), "Read only");

    /**
     * Returns a protection status for the given code.
     *
     * @param code The code to use.
     * @return A {@link ProtectionStatus} for the given code.
     */
    public static ProtectionStatus valueOf(UnsignedShort code) {
        return switch (code.value()) {
            case 0x0000 -> NO_PROTECTION;
            case 0x0001 -> READ_ONLY;
            default -> new ProtectionStatus(code, "Reserved");
        };
    }

}
