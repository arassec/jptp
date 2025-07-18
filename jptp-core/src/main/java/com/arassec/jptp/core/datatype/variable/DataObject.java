package com.arassec.jptp.core.datatype.variable;

import java.nio.ByteBuffer;

public record DataObject(byte[] data) implements DataContainerPayload<DataObject> {

    public static final DataObject emptyInstance = new DataObject(new byte[0]);

    @Override
    public DataObject deserialize(ByteBuffer buffer) {
        buffer.position(12);
        ByteBuffer slicedBuffer = buffer.slice();
        byte[] bytes = new byte[slicedBuffer.remaining()];
        slicedBuffer.get(bytes);
        return new DataObject(bytes);
    }

}
