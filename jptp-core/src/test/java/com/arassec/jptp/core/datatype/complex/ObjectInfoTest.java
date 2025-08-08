package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.datatype.PtpDateTime;
import com.arassec.jptp.core.datatype.PtpString;
import com.arassec.jptp.core.datatype.UnsignedInt;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link ObjectInfo}.
 */
class ObjectInfoTest {

    /**
     * Formats a date and time into PTP format.
     */
    private final DateTimeFormatter dtf = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd'T'HHmmss").toFormatter();

    /**
     * Tests the provided deserializer.
     */
    @Test
    void testDeserializer() {
        ObjectInfo expectedObjectInfo = new ObjectInfo(
                new StorageId(UnsignedInt.valueOf(1)),
                ObjectFormatCode.EXIF_JPEG,
                ProtectionStatus.READ_ONLY,
                new ObjectCompressedSize(UnsignedInt.valueOf(2)),
                ObjectFormatCode.EXIF_JPEG,
                new ThumbCompressedSize(UnsignedInt.valueOf(3)),
                new ThumbPixWidth(UnsignedInt.valueOf(4)),
                new ThumbPixHeight(UnsignedInt.valueOf(5)),
                new ImagePixWidth(UnsignedInt.valueOf(6)),
                new ImagePixHeight(UnsignedInt.valueOf(7)),
                new ImageBitDepth(UnsignedInt.valueOf(8)),
                new ObjectHandle(UnsignedInt.valueOf(9)),
                AssociationCode.GENERIC_FOLDER,
                new AssociationDesc(UnsignedInt.valueOf(10)),
                new SequenceNumber(UnsignedInt.valueOf(11)),
                new Filename(new PtpString("test")),
                new CaptureDate(new PtpDateTime(OffsetDateTime.now()
                        .withOffsetSameInstant(ZoneOffset.UTC)
                        .withNano(0))),
                new ModificationDate(new PtpDateTime(OffsetDateTime.now()
                        .withOffsetSameInstant(ZoneOffset.UTC)
                        .withNano(0))),
                new Keywords(new PtpString("keyword"))
        );

        ByteBuffer buffer = ByteBuffer.allocate(256).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(expectedObjectInfo.storageId().id().value());
        buffer.putShort(expectedObjectInfo.objectFormat().code().value());
        buffer.putShort(expectedObjectInfo.protectionStatus().code().value());
        buffer.putInt(expectedObjectInfo.objectCompressedSize().size().value());
        buffer.putShort(expectedObjectInfo.objectFormat().code().value());
        buffer.putInt(expectedObjectInfo.thumbCompressedSize().size().value());
        buffer.putInt(expectedObjectInfo.thumbPixWidth().width().value());
        buffer.putInt(expectedObjectInfo.thumbPixHeight().height().value());
        buffer.putInt(expectedObjectInfo.imagePixWidth().width().value());
        buffer.putInt(expectedObjectInfo.imagePixHeight().height().value());
        buffer.putInt(expectedObjectInfo.imageBitDepth().depth().value());
        buffer.putInt(expectedObjectInfo.parentObject().handle().value());
        buffer.putShort(expectedObjectInfo.associationType().code().value());
        buffer.putInt(expectedObjectInfo.associationDesc().desc().value());
        buffer.putInt(expectedObjectInfo.sequenceNumber().number().value());
        buffer.put((byte) 8); // String length, two bytes per character!
        buffer.put("test".getBytes(StandardCharsets.UTF_16LE));
        buffer.put((byte) 30); // Formatted date length
        buffer.put(dtf.format(expectedObjectInfo.captureDate().date().dateTime()).getBytes(StandardCharsets.UTF_16LE));
        buffer.put((byte) 30); // Formatted date length
        buffer.put(dtf.format(expectedObjectInfo.modificationDate().date().dateTime()).getBytes(StandardCharsets.UTF_16LE));
        buffer.put((byte) 14); // String length, two bytes per character!
        buffer.put("keyword".getBytes(StandardCharsets.UTF_16LE));
        buffer.rewind();

        ObjectInfo objectInfo = ObjectInfo.DESERIALIZER.deserialize(buffer);

        assertThat(objectInfo).isEqualTo(expectedObjectInfo);
    }

}
