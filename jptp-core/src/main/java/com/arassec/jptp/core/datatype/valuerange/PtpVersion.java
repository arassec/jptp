package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;

/**
 * Value object modeling a PTP version.
 *
 * @param version The version code.
 * @param label The version label.
 */
public record PtpVersion(UnsignedShort version, String label) {

    public static final PtpVersion V1_0 = new PtpVersion(UnsignedShort.valueOf((short) 100), "v1.0");
    public static final PtpVersion V1_1 = new PtpVersion(UnsignedShort.valueOf((short) 110), "v1.1");

    /**
     * Returns an instance for the given version.
     *
     * @param version The version in PTP format, e.g., as UINT16.
     * @return A {@link PtpVersion} for the supplied version code.
     */
    public static PtpVersion valueOf(UnsignedShort version) {
        return switch (version.value()) {
            case 100 -> V1_0;
            case 110 -> V1_1;
            default -> new PtpVersion(version, String.valueOf(version.value()));
        };
    }

}
