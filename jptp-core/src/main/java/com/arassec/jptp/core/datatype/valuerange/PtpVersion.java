package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;

public record PtpVersion(UnsignedShort version, String label) {

    public static final PtpVersion V1_0 = new PtpVersion(UnsignedShort.valueOf((short) 100), "v1.0");
    public static final PtpVersion V1_1 = new PtpVersion(UnsignedShort.valueOf((short) 110), "v1.1");

    public static PtpVersion valueOf(UnsignedShort version) {
        return switch (version.value()) {
            case 100 -> V1_0;
            case 110 -> V1_1;
            default -> new PtpVersion(version, "TODO");
        };
    }

}
