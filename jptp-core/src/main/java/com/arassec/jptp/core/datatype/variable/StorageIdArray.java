package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.datatype.UnsignedInt;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public record StorageIdArray(List<StorageId> ids) implements PtpContainerPayload<StorageIdArray> {

    public static StorageIdArray emptyInstance = new StorageIdArray(List.of());

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
