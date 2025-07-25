package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;

/**
 * Value object modeling the PTP access capabilities.
 *
 * @param code        The code.
 * @param description The description.
 */
public record AccessCapability(UnsignedShort code, String description) {

    public static final AccessCapability READ_WRITE = new AccessCapability(UnsignedShort.valueOf((short) 0x0000), "Read-write");
    public static final AccessCapability READ_ONLY_WITHOUT_OBJECT_DELETION = new AccessCapability(UnsignedShort.valueOf((short) 0x0001), "Read-only without object deletion");
    public static final AccessCapability READ_ONLY_WITH_OBJECT_DELETION = new AccessCapability(UnsignedShort.valueOf((short) 0x0002), "Read-only with object deletion");

    /**
     * Returns an instance for the given code.
     *
     * @param code The code to use.
     * @return An {@link AccessCapability} instance for the given code.
     */
    public static AccessCapability valueOf(UnsignedShort code) {
        return switch (code.value()) {
            case 0x0000 -> READ_WRITE;
            case 0x0001 -> READ_ONLY_WITHOUT_OBJECT_DELETION;
            case 0x0002 -> READ_ONLY_WITH_OBJECT_DELETION;
            default -> new AccessCapability(code, "Reserved");
        };
    }

}
