package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.datatype.PtpString;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedLong;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link StorageInfo}.
 */
class StorageInfoTest {

    /**
     * Tests deserialization.
     */
    @Test
    void testDeserialize() {
        StorageInfo expectedStorageInfo = new StorageInfo(
                StorageType.FIXED_ROM,
                FilesystemType.GENERIC_FLAT,
                AccessCapability.READ_WRITE,
                new MaxCapacity(UnsignedLong.valueOf(1)),
                new FreeSpaceInBytes(UnsignedLong.valueOf(2)),
                new FreeSpaceInImages(UnsignedInt.valueOf(3)),
                new StorageDescription(new PtpString("desc")),
                new VolumeLabel(new PtpString("label"))
        );

        ByteBuffer buffer = ByteBuffer.allocate(256).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(expectedStorageInfo.storageType().code().value());
        buffer.putShort(expectedStorageInfo.filesystemType().code().value());
        buffer.putShort(expectedStorageInfo.accessCapability().code().value());
        buffer.putLong(expectedStorageInfo.maxCapacity().capacity().value());
        buffer.putLong(expectedStorageInfo.freeSpaceInBytes().freeSpace().value());
        buffer.putInt(expectedStorageInfo.freeSpaceInImages().freeSpace().value());
        buffer.put((byte) 4); // String length, number of characters in the string!
        buffer.put("desc".getBytes(StandardCharsets.UTF_16LE));
        buffer.put((byte) 5); // String length, number of characters in the string!
        buffer.put("label".getBytes(StandardCharsets.UTF_16LE));
        buffer.rewind();

        StorageInfo storageInfo = StorageInfo.DESERIALIZER.deserialize(buffer);

        assertThat(storageInfo).isEqualTo(expectedStorageInfo);
    }

}
