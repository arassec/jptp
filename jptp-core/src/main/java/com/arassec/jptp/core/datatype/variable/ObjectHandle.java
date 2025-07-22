package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.datatype.UnsignedInt;

import java.nio.ByteBuffer;

public record ObjectHandle(UnsignedInt handle) implements PtpContainerPayload<ObjectHandle> {

    public static ObjectHandle emptyInstance() {
        return new ObjectHandle(null);
    }

    @Override
    public ObjectHandle deserialize(ByteBuffer buffer) {
        return new ObjectHandle(UnsignedInt.deserialize(buffer));
    }

}
