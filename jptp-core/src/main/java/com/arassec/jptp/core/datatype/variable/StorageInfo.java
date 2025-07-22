package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.datatype.PtpString;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedLong;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.valuerange.AccessCapability;
import com.arassec.jptp.core.datatype.valuerange.FilesystemType;
import com.arassec.jptp.core.datatype.valuerange.StorageType;

import java.nio.ByteBuffer;

public record StorageInfo(
        StorageType storageType,
        FilesystemType filesystemType,
        AccessCapability accessCapability,
        MaxCapacity maxCapacity,
        FreeSpaceInBytes freeSpaceInBytes,
        FreeSpaceInImages freeSpaceInImages,
        StorageDescription storageDescription,
        VolumeLabel volumeLabel
) implements PtpContainerPayload<StorageInfo> {

    public static StorageInfo emptyInstance = new StorageInfo(null, null, null,
            null, null, null, null, null);

    @Override
    public StorageInfo deserialize(ByteBuffer buffer) {
        return new StorageInfo(
                StorageType.valueOf(UnsignedShort.deserialize(buffer)),
                FilesystemType.valueOf(UnsignedShort.deserialize(buffer)),
                AccessCapability.valueOf(UnsignedShort.deserialize(buffer)),
                new MaxCapacity(UnsignedLong.deserialize(buffer)),
                new FreeSpaceInBytes(UnsignedLong.deserialize(buffer)),
                new FreeSpaceInImages(UnsignedInt.deserialize(buffer)),
                new StorageDescription(PtpString.deserialize(buffer)),
                new VolumeLabel(PtpString.deserialize(buffer))
        );
    }

}
