package com.arassec.jptp.core.datatype.payload;

import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.simple.StorageId;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * The 'Storage ID Array' data type.
 *
 * @param ids The storage IDs as List.
 */
public record StorageIdArray(List<StorageId> ids) implements PtpContainerPayload<StorageIdArray> {

    /**
     * An empty instance to support deserialization.
     */
    public static StorageIdArray emptyInstance = new StorageIdArray(List.of());

    /**
     * {@inheritDoc}
     */
    @Override
    public StorageIdArray deserialize(ByteBuffer buffer) {
        List<StorageId> ids = new ArrayList<>();
        if (buffer.hasRemaining()) {
            int arraySize = buffer.getInt();
            for (int i = 0; i < arraySize; i++) {
                ids.add(new StorageId(UnsignedInt.deserialize(buffer)));
            }
        }
        return new StorageIdArray(ids);
    }

}
