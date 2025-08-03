package com.arassec.jptp.core.datatype.simple;

import com.arassec.jptp.core.datatype.PtpString;

/**
 * The 'Vendor extension description' data type.
 *
 * @param description The description as string.
 */
public record VendorExtensionDescription(PtpString description) {
}
