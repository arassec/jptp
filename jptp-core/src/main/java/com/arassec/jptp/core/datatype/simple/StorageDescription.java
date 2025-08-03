package com.arassec.jptp.core.datatype.simple;

import com.arassec.jptp.core.datatype.PtpString;

/**
 * The 'Storage description' data type.
 *
 * @param description The description as string.
 */
public record StorageDescription(PtpString description) {
}
