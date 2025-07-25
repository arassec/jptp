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

/**
 * The 'Storage Info' data set.
 *
 * @param storageType        The storage type.
 * @param filesystemType     The storage's filesystem type.
 * @param accessCapability   The storage's access capability.
 * @param maxCapacity        The storage's maximum capacity.
 * @param freeSpaceInBytes   The storage's free space in bytes.
 * @param freeSpaceInImages  The storage's free space in images.
 * @param storageDescription The storage description.
 * @param volumeLabel        The storage's volume label.
 */
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

    /**
     * An empty instance for deserialization purposes.
     */
    public static StorageInfo emptyInstance = new StorageInfo(null, null, null,
            null, null, null, null, null);

    /**
     * {@inheritDoc}
     */
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
