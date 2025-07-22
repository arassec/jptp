package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.PtpContainerPayload;

import java.nio.ByteBuffer;

public record DataObject(byte[] data) implements PtpContainerPayload<DataObject> {

    public static final DataObject emptyInstance = new DataObject(new byte[0]);

    @Override
    public DataObject deserialize(ByteBuffer buffer) {
        // TODO: Check if necessary: buffer.position(12);
        ByteBuffer slicedBuffer = buffer.slice();
        byte[] bytes = new byte[slicedBuffer.remaining()];
        slicedBuffer.get(bytes);
        return new DataObject(bytes);
    }

}
