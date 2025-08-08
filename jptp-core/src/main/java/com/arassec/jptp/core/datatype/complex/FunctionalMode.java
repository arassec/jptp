package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.datatype.UnsignedShort;

/**
 * Value object modeling a PTP functional mode.
 *
 * @param mode        The mode.
 * @param description A description of the mode.
 */
public record FunctionalMode(UnsignedShort mode, String description) {

    public static FunctionalMode STANDARD = new FunctionalMode(UnsignedShort.valueOf((short) 0x0000), "Standard mode");
    public static FunctionalMode SLEEP = new FunctionalMode(UnsignedShort.valueOf((short) 0x0001), "Sleep mode");

    /**
     * Returns an instance for the given mode.
     *
     * @param mode The mode to use.
     * @return A {@link FunctionalMode} for the given mode.
     */
    public static FunctionalMode valueOf(UnsignedShort mode) {
        return switch (mode.value()) {
            case 0x0000 -> STANDARD;
            case 0x0001 -> SLEEP;
            default -> new FunctionalMode(mode, "Reserved / Vendor-defined");
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "FunctionalMode[mode='" + String.format("0x%04X", mode.value()) + "', description='" + description + "']";
    }

}
