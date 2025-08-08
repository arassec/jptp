package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.PayloadDeserializer;
import com.arassec.jptp.core.datatype.UnsignedInt;

import java.util.ArrayList;
import java.util.List;

/**
 * The 'Object handle array' data type.
 *
 * @param handles List of {@link ObjectHandle}s.
 */
public record ObjectHandleArray(List<ObjectHandle> handles) {

    /**
     * {@link PayloadDeserializer} for this record.
     */
    public static final PayloadDeserializer<ObjectHandleArray> DESERIALIZER = buffer -> {
        List<ObjectHandle> handles = new ArrayList<>();
        if (buffer.hasRemaining()) {
            int arraySize = buffer.getInt();
            for (int i = 0; i < arraySize; i++) {
                handles.add(new ObjectHandle(UnsignedInt.deserialize(buffer)));
            }
        }
        return new ObjectHandleArray(handles);
    };

}
