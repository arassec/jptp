package com.arassec.jptp.core.datatype.simple;

import com.arassec.jptp.core.datatype.PtpString;

/**
 * The 'device version' data type.
 *
 * @param value The version value.
 */
public record DeviceVersion(PtpString value) {
}
