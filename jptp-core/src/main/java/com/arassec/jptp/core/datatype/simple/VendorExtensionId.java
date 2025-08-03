package com.arassec.jptp.core.datatype.simple;

import com.arassec.jptp.core.datatype.UnsignedInt;

/**
 * The 'Vendor extension ID' data type.
 *
 * @param id The ID as UINT32.
 */
public record VendorExtensionId(UnsignedInt id) {
}
