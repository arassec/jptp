package com.arassec.jptp.core;

import java.nio.ByteBuffer;

public interface PtpContainerPayload<P> {

    P deserialize(ByteBuffer buffer);

}
