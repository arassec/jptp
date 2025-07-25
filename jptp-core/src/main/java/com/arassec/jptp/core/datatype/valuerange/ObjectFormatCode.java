package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Value object for a PTP object format code.
 *
 * @param code        The code.
 * @param type        The type.
 * @param format      The format.
 * @param description A description.
 */
public record ObjectFormatCode(UnsignedShort code, char type, String format, String description) {

    public static ObjectFormatCode UNDEFINED = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3000), 'A', "Undefined", "Undefined non-image object");
    public static ObjectFormatCode ASSOCIATION = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3001), 'A', "Association", "Association (e.g. folder)");
    public static ObjectFormatCode SCRIPT = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3002), 'A', "Script", "Device-model-specific script");
    public static ObjectFormatCode EXECUTABLE = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3003), 'A', "Executable", "Device-model-specific binary executable");
    public static ObjectFormatCode TEXT = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3004), 'A', "Text", "Text file");
    public static ObjectFormatCode HTML = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3005), 'A', "HTML", "HyperText Markup Language file (text)");
    public static ObjectFormatCode DPOF = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3006), 'A', "DPOF", "Digital Print Order Format file (text)");
    public static ObjectFormatCode AIFF = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3007), 'A', "AIFF", "Audio clip");
    public static ObjectFormatCode WAV = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3008), 'A', "WAV", "Audio clip");
    public static ObjectFormatCode MP3 = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3009), 'A', "MP3", "Audio clip");
    public static ObjectFormatCode AVI = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x300A), 'A', "AVI", "Video clip");
    public static ObjectFormatCode MPEG = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x300B), 'A', "MPEG", "Video clip");
    public static ObjectFormatCode ASF = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x300C), 'A', "ASF", "Microsoft Advanced Streaming Format (video)");
    public static ObjectFormatCode QUICKTIME = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x300D), 'A', "QuickTime", "Apple QuickTime video format");
    public static ObjectFormatCode XML = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x300E), 'A', "XML", "eXtensible Markup Language file");
    public static ObjectFormatCode UNKNOWN = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3800), 'I', "Undefined", "Unknown image object");
    public static ObjectFormatCode EXIF_JPEG = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3801), 'I', "Exif/JPEG", "Exchangeable File Format. JEITA standard");
    public static ObjectFormatCode TIFF_EP = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3802), 'I', "TIFF/EP", "Tag Image File Format for Electronic Photography");
    public static ObjectFormatCode FLASHPIX = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3803), 'I', "FlashPix", "Structured Storage Image Format");
    public static ObjectFormatCode BMP = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3804), 'I', "BMP", "Microsoft Windows Bitmap file");
    public static ObjectFormatCode CIFF = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3805), 'I', "CIFF", "Canon Camera Image File Format");
    public static ObjectFormatCode RESERVED = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3806), 'I', "Undefined", "Reserved");
    public static ObjectFormatCode GIF = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3807), 'I', "GIF", "Graphics Interchange Format");
    public static ObjectFormatCode JFIF = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3808), 'I', "JFIF", "JPEG File Interchange Format");
    public static ObjectFormatCode PCD = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3809), 'I', "PCD", "PhotoCD Image Pac");
    public static ObjectFormatCode PICT = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x380A), 'I', "PICT", "Quickdraw Image Format");
    public static ObjectFormatCode PNG = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x380B), 'I', "PNG", "Portable Network Graphics");
    public static ObjectFormatCode RESERVED_TWO = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x380C), 'I', "Undefined", "Reserved");
    public static ObjectFormatCode TIFF = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x380D), 'I', "TIFF", "Tag Image File Format");
    public static ObjectFormatCode TIFF_IT = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x380E), 'I', "TIFF/IT", "Tag Image File Format for Image Technology");
    public static ObjectFormatCode JP2 = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x380F), 'I', "JP2", "JPEG2000 Baseline File Format");
    public static ObjectFormatCode JPX = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3810), 'I', "JPX", "JPEG2000 Extended File Format");
    public static ObjectFormatCode DNG = new ObjectFormatCode(UnsignedShort.valueOf((short) 0x3811), 'I', "DNG", "Digital Negative Format (PTP v1.1");

    /**
     * Returns an instance for the given code.
     *
     * @param code The code to use.
     * @return An {@link ObjectFormatCode} for the given code.
     */
    public static ObjectFormatCode valueOf(UnsignedShort code) {
        return switch (code.value()) {
            case 0x3000 -> UNDEFINED;
            case 0x3001 -> ASSOCIATION;
            case 0x3002 -> SCRIPT;
            case 0x3003 -> EXECUTABLE;
            case 0x3004 -> TEXT;
            case 0x3005 -> HTML;
            case 0x3006 -> DPOF;
            case 0x3007 -> AIFF;
            case 0x3008 -> WAV;
            case 0x3009 -> MP3;
            case 0x300A -> AVI;
            case 0x300B -> MPEG;
            case 0x300C -> ASF;
            case 0x300D -> QUICKTIME;
            case 0x300E -> XML;
            case 0x3800 -> UNKNOWN;
            case 0x3801 -> EXIF_JPEG;
            case 0x3802 -> TIFF_EP;
            case 0x3803 -> FLASHPIX;
            case 0x3804 -> BMP;
            case 0x3805 -> CIFF;
            case 0x3806 -> RESERVED;
            case 0x3807 -> GIF;
            case 0x3808 -> JFIF;
            case 0x3809 -> PCD;
            case 0x380A -> PICT;
            case 0x380B -> PNG;
            case 0x380C -> RESERVED_TWO;
            case 0x380D -> TIFF;
            case 0x380E -> TIFF_IT;
            case 0x380F -> JP2;
            case 0x3810 -> JPX;
            case 0x3811 -> DNG;
            default -> new ObjectFormatCode(code, 'X', "TODO", "TODO");
        };
    }

    /**
     * Deserializes the supplied byte buffer into a list of codes.
     *
     * @param buffer The {@link ByteBuffer} containing a PTP array of object format codes.
     * @return A list of corresponding {@link ObjectFormatCode}s.
     */
    public static List<ObjectFormatCode> deserializeArray(ByteBuffer buffer) {
        List<ObjectFormatCode> operationCodes = new ArrayList<>();
        for (int i = 0; i < buffer.getInt(); i++) {
            operationCodes.add(ObjectFormatCode.valueOf(UnsignedShort.deserialize(buffer)));
        }
        return operationCodes;
    }

}
