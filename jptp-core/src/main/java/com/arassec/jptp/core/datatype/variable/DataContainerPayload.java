package com.arassec.jptp.core.datatype.variable;

import java.nio.ByteBuffer;

public interface DataContainerPayload<P> {

    P deserialize(ByteBuffer buffer);

}
