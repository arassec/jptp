package com.arassec.jptp.core.datatype.simple;

import com.arassec.jptp.core.datatype.UnsignedShort;

/**
 * The 'Vendor extension version' data type.
 *
 * @param version The version as UINT16.
 */
public record VendorExtensionVersion(UnsignedShort version) {
}
