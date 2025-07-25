package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.datatype.UnsignedInt;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * The 'Object handle array' data type.
 *
 * @param handles List of {@link ObjectHandle}s.
 */
public record ObjectHandleArray(List<ObjectHandle> handles) implements PtpContainerPayload<ObjectHandleArray> {

    /**
     * An empty instance for deserialization purposes.
     */
    public static ObjectHandleArray emptyInstance = new ObjectHandleArray(List.of());

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectHandleArray deserialize(ByteBuffer buffer) {
        List<ObjectHandle> handles = new ArrayList<>();
        if (buffer.hasRemaining()) {
            int arraySize = buffer.getInt();
            for (int i = 0; i < arraySize; i++) {
                handles.add(new ObjectHandle(UnsignedInt.deserialize(buffer)));
            }
        }
        return new ObjectHandleArray(handles);
    }

}
