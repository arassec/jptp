package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.datatype.UnsignedShort;

/**
 * Value object modeling the PTP association codes.
 *
 * @param code           The code.
 * @param type           The type.
 * @param interpretation The interpretation.
 */
public record AssociationCode(UnsignedShort code, String type, String interpretation) {

    private static final String UNDEFINED_LABEL = "Undefined";
    private static final String UNUSED_LABEL = "Unused";

    public static final AssociationCode UNDEFINED = new AssociationCode(UnsignedShort.valueOf((short) 0x0000), UNDEFINED_LABEL, UNDEFINED_LABEL);
    public static final AssociationCode GENERIC_FOLDER = new AssociationCode(UnsignedShort.valueOf((short) 0x0001), "GenericFolder", UNUSED_LABEL);
    public static final AssociationCode ALBUM = new AssociationCode(UnsignedShort.valueOf((short) 0x0002), "Album", "Reserved");
    public static final AssociationCode TIME_SEQUENCE = new AssociationCode(UnsignedShort.valueOf((short) 0x0003), "TimeSequence", "DefaultPlaybackDelta");
    public static final AssociationCode HORIZONTAL_PANORAMIC = new AssociationCode(UnsignedShort.valueOf((short) 0x0004), "HorizontalPanoramic", UNUSED_LABEL);
    public static final AssociationCode VERTICAL_PANORAMIC = new AssociationCode(UnsignedShort.valueOf((short) 0x0005), "VerticalPanoramic", UNUSED_LABEL);
    public static final AssociationCode TWO_D_PANORAMIC = new AssociationCode(UnsignedShort.valueOf((short) 0x0006), "2DPanoramic", "ImagesPerRow");
    public static final AssociationCode ANCILLARY_DATA = new AssociationCode(UnsignedShort.valueOf((short) 0x0007), "AncillaryData", UNDEFINED_LABEL);

    /**
     * Returns an instance for the given code.
     *
     * @param code The code to use.
     * @return An {@link AssociationCode} instance for the given code.
     */
    public static AssociationCode valueOf(UnsignedShort code) {
        return switch (code.value()) {
            case 0x0000 -> UNDEFINED;
            case 0x0001 -> GENERIC_FOLDER;
            case 0x0002 -> ALBUM;
            case 0x0003 -> TIME_SEQUENCE;
            case 0x0004 -> HORIZONTAL_PANORAMIC;
            case 0x0005 -> VERTICAL_PANORAMIC;
            case 0x0006 -> TWO_D_PANORAMIC;
            case 0x0007 -> ANCILLARY_DATA;
            default -> new AssociationCode(code, "Reserved / Vendor-defined", "Undefined / Vendor-defined");
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "AssociationCode[code='" + String.format("0x%04X", code.value()) + "', type='" + type + "']";
    }

}
