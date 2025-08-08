package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.datatype.UnsignedShort;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Value object modeling a PTP operation code.
 *
 * @param code       The code.
 * @param name       The code's name.
 * @param ptpVersion The PTP version introducing the code.
 */
public record OperationCode(UnsignedShort code, String name, Version ptpVersion) {

    public static final OperationCode UNDEFINED = new OperationCode(UnsignedShort.valueOf((short) 0x1000), "Undefined", Version.V1_0);
    public static final OperationCode GET_DEVICE_INFO = new OperationCode(UnsignedShort.valueOf((short) 0x1001), "GetDeviceInfo", Version.V1_0);
    public static final OperationCode OPEN_SESSION = new OperationCode(UnsignedShort.valueOf((short) 0x1002), "OpenSession", Version.V1_0);
    public static final OperationCode CLOSE_SESSION = new OperationCode(UnsignedShort.valueOf((short) 0x1003), "CloseSession", Version.V1_0);
    public static final OperationCode GET_STORAGE_IDS = new OperationCode(UnsignedShort.valueOf((short) 0x1004), "GetStorageIDs", Version.V1_0);
    public static final OperationCode GET_STORAGE_INFO = new OperationCode(UnsignedShort.valueOf((short) 0x1005), "GetStorageInfo", Version.V1_0);
    public static final OperationCode GET_NUM_OBJECTS = new OperationCode(UnsignedShort.valueOf((short) 0x1006), "GetNumObjects", Version.V1_0);
    public static final OperationCode GET_OBJECT_HANDLES = new OperationCode(UnsignedShort.valueOf((short) 0x1007), "GetObjectHandles", Version.V1_0);
    public static final OperationCode GET_OBJECT_INFO = new OperationCode(UnsignedShort.valueOf((short) 0x1008), "GetObjectInfo", Version.V1_0);
    public static final OperationCode GET_OBJECT = new OperationCode(UnsignedShort.valueOf((short) 0x1009), "GetObject", Version.V1_0);
    public static final OperationCode GET_THUMB = new OperationCode(UnsignedShort.valueOf((short) 0x100A), "GetThumb", Version.V1_0);
    public static final OperationCode DELETE_OBJECT = new OperationCode(UnsignedShort.valueOf((short) 0x100B), "DeleteObject", Version.V1_0);
    public static final OperationCode SEND_OBJECT_INFO = new OperationCode(UnsignedShort.valueOf((short) 0x100C), "SendObjectInfo", Version.V1_0);
    public static final OperationCode SEND_OBJECT = new OperationCode(UnsignedShort.valueOf((short) 0x100D), "SendObject", Version.V1_0);
    public static final OperationCode INITIATE_CAPTURE = new OperationCode(UnsignedShort.valueOf((short) 0x100E), "InitiateCapture", Version.V1_0);
    public static final OperationCode FORMAT_STORE = new OperationCode(UnsignedShort.valueOf((short) 0x100F), "FormatStore", Version.V1_0);
    public static final OperationCode RESET_DEVICE = new OperationCode(UnsignedShort.valueOf((short) 0x1010), "ResetDevice", Version.V1_0);
    public static final OperationCode SELF_TEST = new OperationCode(UnsignedShort.valueOf((short) 0x1011), "SelfTest", Version.V1_0);
    public static final OperationCode SET_OBJECT_PROTECTION = new OperationCode(UnsignedShort.valueOf((short) 0x1012), "SetObjectProtection", Version.V1_0);
    public static final OperationCode POWER_DOWN = new OperationCode(UnsignedShort.valueOf((short) 0x1013), "PowerDown", Version.V1_0);
    public static final OperationCode GET_DEVICE_PROP_DESC = new OperationCode(UnsignedShort.valueOf((short) 0x1014), "GetDevicePropDesc", Version.V1_0);
    public static final OperationCode GET_DEVICE_PROP_VALUE = new OperationCode(UnsignedShort.valueOf((short) 0x1015), "GetDevicePropValue", Version.V1_0);
    public static final OperationCode SET_DEVICE_PROP_VALUE = new OperationCode(UnsignedShort.valueOf((short) 0x1016), "SetDevicePropValue", Version.V1_0);
    public static final OperationCode RESET_DEVICE_PROP_VALUE = new OperationCode(UnsignedShort.valueOf((short) 0x1017), "ResetDevicePropValue", Version.V1_0);
    public static final OperationCode TERMINATE_OPEN_CAPTURE = new OperationCode(UnsignedShort.valueOf((short) 0x1018), "TerminateOpenCapture", Version.V1_0);
    public static final OperationCode MOVE_OBJECT = new OperationCode(UnsignedShort.valueOf((short) 0x1019), "MoveObject", Version.V1_0);
    public static final OperationCode COPY_OBJECT = new OperationCode(UnsignedShort.valueOf((short) 0x101A), "CopyObject", Version.V1_0);
    public static final OperationCode GET_PARTIAL_OBJECT = new OperationCode(UnsignedShort.valueOf((short) 0x101B), "GetPartialObject", Version.V1_0);
    public static final OperationCode INITIATE_OPEN_CAPTURE = new OperationCode(UnsignedShort.valueOf((short) 0x101C), "InitiateOpenCapture", Version.V1_0);
    public static final OperationCode START_ENUM_HANDLES = new OperationCode(UnsignedShort.valueOf((short) 0x101D), "StartEnumHandles", Version.V1_1);
    public static final OperationCode ENUM_HANDLES = new OperationCode(UnsignedShort.valueOf((short) 0x101E), "EnumHandles", Version.V1_1);
    public static final OperationCode STOP_ENUM_HANDLES = new OperationCode(UnsignedShort.valueOf((short) 0x101F), "StopEnumHandles", Version.V1_1);
    public static final OperationCode GET_VENDOR_EXTENSION_MAPS = new OperationCode(UnsignedShort.valueOf((short) 0x1020), "GetVendorExtensionMaps", Version.V1_1);
    public static final OperationCode GET_VENDOR_DEVICE_INFO = new OperationCode(UnsignedShort.valueOf((short) 0x1021), "GetVendorDeviceInfo", Version.V1_1);
    public static final OperationCode GET_RESIZED_IMAGE_OBJECT = new OperationCode(UnsignedShort.valueOf((short) 0x1022), "GetResizedImageObject", Version.V1_1);
    public static final OperationCode GET_FILESYSTEM_MANIFEST = new OperationCode(UnsignedShort.valueOf((short) 0x1023), "GetFileSystemManifest", Version.V1_1);
    public static final OperationCode GET_STREAM_INFO = new OperationCode(UnsignedShort.valueOf((short) 0x1024), "GetStreamInfo", Version.V1_1);
    public static final OperationCode GET_STREAM = new OperationCode(UnsignedShort.valueOf((short) 0x1025), "GetStream", Version.V1_1);

    /**
     * Returns an operation code for the given code.
     *
     * @param code The code to use.
     * @return An {@link OperationCode} instance for the given code.
     */
    @SuppressWarnings("java:S1479") // The number of switch cases will be reduced.
    public static OperationCode valueOf(UnsignedShort code) {
        return switch (code.value()) {
            case 0x1000 -> UNDEFINED;
            case 0x1001 -> GET_DEVICE_INFO;
            case 0x1002 -> OPEN_SESSION;
            case 0x1003 -> CLOSE_SESSION;
            case 0x1004 -> GET_STORAGE_IDS;
            case 0x1005 -> GET_STORAGE_INFO;
            case 0x1006 -> GET_NUM_OBJECTS;
            case 0x1007 -> GET_OBJECT_HANDLES;
            case 0x1008 -> GET_OBJECT_INFO;
            case 0x1009 -> GET_OBJECT;
            case 0x100A -> GET_THUMB;
            case 0x100B -> DELETE_OBJECT;
            case 0x100C -> SEND_OBJECT_INFO;
            case 0x100D -> SEND_OBJECT;
            case 0x100E -> INITIATE_CAPTURE;
            case 0x100F -> FORMAT_STORE;
            case 0x1010 -> RESET_DEVICE;
            case 0x1011 -> SELF_TEST;
            case 0x1012 -> SET_OBJECT_PROTECTION;
            case 0x1013 -> POWER_DOWN;
            case 0x1014 -> GET_DEVICE_PROP_DESC;
            case 0x1015 -> GET_DEVICE_PROP_VALUE;
            case 0x1016 -> SET_DEVICE_PROP_VALUE;
            case 0x1017 -> RESET_DEVICE_PROP_VALUE;
            case 0x1018 -> TERMINATE_OPEN_CAPTURE;
            case 0x1019 -> MOVE_OBJECT;
            case 0x101A -> COPY_OBJECT;
            case 0x101B -> GET_PARTIAL_OBJECT;
            case 0x101C -> INITIATE_OPEN_CAPTURE;
            case 0x101D -> START_ENUM_HANDLES;
            case 0x101E -> ENUM_HANDLES;
            case 0x101F -> STOP_ENUM_HANDLES;
            case 0x1020 -> GET_VENDOR_EXTENSION_MAPS;
            case 0x1021 -> GET_VENDOR_DEVICE_INFO;
            case 0x1022 -> GET_RESIZED_IMAGE_OBJECT;
            case 0x1023 -> GET_FILESYSTEM_MANIFEST;
            case 0x1024 -> GET_STREAM_INFO;
            case 0x1025 -> GET_STREAM;
            default -> new OperationCode(code, "Vendor Specific", Version.V1_0);
        };
    }

    /**
     * Deserializes the given byte buffer into a list of operation codes.
     *
     * @param buffer The {@link ByteBuffer} containing a PTP array with operation codes.
     * @return A list of {@link OperationCode}s from the buffer.
     */
    public static List<OperationCode> deserializeArray(ByteBuffer buffer) {
        List<OperationCode> operationCodes = new ArrayList<>();
        int arrayLength = buffer.getInt();
        for (int i = 0; i < arrayLength; i++) {
            operationCodes.add(OperationCode.valueOf(UnsignedShort.deserialize(buffer)));
        }
        return operationCodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "OperationCode[code='" + String.format("0x%04X", code.value()) + "', name='" + name + "']";
    }

}
