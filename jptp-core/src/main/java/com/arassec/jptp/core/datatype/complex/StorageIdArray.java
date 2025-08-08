package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.PayloadDeserializer;
import com.arassec.jptp.core.datatype.UnsignedInt;

import java.util.ArrayList;
import java.util.List;

/**
 * The 'Storage ID Array' data type.
 *
 * @param ids The storage IDs as List.
 */
public record StorageIdArray(List<StorageId> ids) {

    /**
     * {@link PayloadDeserializer} for this record.
     */
    public static final PayloadDeserializer<StorageIdArray> DESERIALIZER = buffer -> {
        List<StorageId> ids = new ArrayList<>();
        if (buffer.hasRemaining()) {
            int arraySize = buffer.getInt();
            for (int i = 0; i < arraySize; i++) {
                ids.add(new StorageId(UnsignedInt.deserialize(buffer)));
            }
        }
        return new StorageIdArray(ids);
    };

}
