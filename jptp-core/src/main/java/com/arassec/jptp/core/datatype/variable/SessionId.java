package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.datatype.UnsignedInt;

/**
 * The 'Session ID' data type.
 *
 * @param id The session ID as UINT32.
 */
public record SessionId(UnsignedInt id) {
}
