package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.PayloadDeserializer;
import com.arassec.jptp.core.datatype.PtpDateTime;
import com.arassec.jptp.core.datatype.PtpString;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;

/**
 * The 'Object Info' data set.
 *
 * @param storageId            The ID of the storage this object lies on.
 * @param objectFormat         The object's format.
 * @param protectionStatus     The protection status.
 * @param objectCompressedSize The object's compressed size.
 * @param thumbFormat          The thumbnail format.
 * @param thumbCompressedSize  The thumbnails compressed size.
 * @param thumbPixWidth        The thumbnails width in pixels.
 * @param thumbPixHeight       The thumbnails height in pixels.
 * @param imagePixWidth        The image width in pixels.
 * @param imagePixHeight       The image height in pixels.
 * @param imageBitDepth        The image's bit depth.
 * @param parentObject         The object's parent.
 * @param associationType      The object's association type.
 * @param associationDesc      The object's association description.
 * @param sequenceNumber       the object's sequence number.
 * @param filename             The filename.
 * @param captureDate          The capture date.
 * @param modificationDate     The modification date.
 * @param keywords             The object's keywords.
 */
public record ObjectInfo(
        StorageId storageId,
        ObjectFormatCode objectFormat,
        ProtectionStatus protectionStatus,
        ObjectCompressedSize objectCompressedSize,
        ObjectFormatCode thumbFormat,
        ThumbCompressedSize thumbCompressedSize,
        ThumbPixWidth thumbPixWidth,
        ThumbPixHeight thumbPixHeight,
        ImagePixWidth imagePixWidth,
        ImagePixHeight imagePixHeight,
        ImageBitDepth imageBitDepth,
        ObjectHandle parentObject,
        AssociationCode associationType,
        AssociationDesc associationDesc,
        SequenceNumber sequenceNumber,
        Filename filename,
        CaptureDate captureDate,
        ModificationDate modificationDate,
        Keywords keywords) {

    /**
     * {@link PayloadDeserializer} for this record.
     */
    public static final PayloadDeserializer<ObjectInfo> DESERIALIZER = buffer -> new ObjectInfo(
            new StorageId(UnsignedInt.deserialize(buffer)),
            ObjectFormatCode.valueOf(UnsignedShort.deserialize(buffer)),
            ProtectionStatus.valueOf(UnsignedShort.deserialize(buffer)),
            new ObjectCompressedSize(UnsignedInt.deserialize(buffer)),
            ObjectFormatCode.valueOf(UnsignedShort.deserialize(buffer)),
            new ThumbCompressedSize(UnsignedInt.deserialize(buffer)),
            new ThumbPixWidth(UnsignedInt.deserialize(buffer)),
            new ThumbPixHeight(UnsignedInt.deserialize(buffer)),
            new ImagePixWidth(UnsignedInt.deserialize(buffer)),
            new ImagePixHeight(UnsignedInt.deserialize(buffer)),
            new ImageBitDepth(UnsignedInt.deserialize(buffer)),
            new ObjectHandle(UnsignedInt.deserialize(buffer)),
            AssociationCode.valueOf(UnsignedShort.deserialize(buffer)),
            new AssociationDesc(UnsignedInt.deserialize(buffer)),
            new SequenceNumber(UnsignedInt.deserialize(buffer)),
            new Filename(PtpString.deserialize(buffer)),
            new CaptureDate(PtpDateTime.deserialize(buffer)),
            new ModificationDate(PtpDateTime.deserialize(buffer)),
            new Keywords(PtpString.deserialize(buffer))
    );

}
