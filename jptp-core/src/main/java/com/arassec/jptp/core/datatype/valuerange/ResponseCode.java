package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;

/**
 * Value object for PTP response codes.
 *
 * @param code        The response code.
 * @param description A description of the code.
 */
public record ResponseCode(UnsignedShort code, String description) {

    public static ResponseCode UNDEFINED = new ResponseCode(UnsignedShort.valueOf((short) 0x2000), "Undefined");
    public static ResponseCode OK = new ResponseCode(UnsignedShort.valueOf((short) 0x2001), "OK");
    public static ResponseCode GENERAL_ERROR = new ResponseCode(UnsignedShort.valueOf((short) 0x2002), "General Error");
    public static ResponseCode SESSION_NOT_OPEN = new ResponseCode(UnsignedShort.valueOf((short) 0x2003), "Session not open");
    public static ResponseCode INVALID_TRANSACTION_ID = new ResponseCode(UnsignedShort.valueOf((short) 0x2004), "Invalid TransactionID");
    public static ResponseCode OPERATION_NOT_SUPPORTED = new ResponseCode(UnsignedShort.valueOf((short) 0x2005), "Operation not supported");
    public static ResponseCode PARAMETER_NOT_SUPPORTED = new ResponseCode(UnsignedShort.valueOf((short) 0x2006), "Parameter not supported");
    public static ResponseCode INCOMPLETE_TRANSFER = new ResponseCode(UnsignedShort.valueOf((short) 0x2007), "Incomplete transfer");
    public static ResponseCode INVALID_STORAGE_ID = new ResponseCode(UnsignedShort.valueOf((short) 0x2008), "Invalid StorageID");
    public static ResponseCode INVALID_OBJECT_HANDLE = new ResponseCode(UnsignedShort.valueOf((short) 0x2009), "Invalid ObjectHandle");
    public static ResponseCode DEVICE_PROP_NOT_SUPPORTED = new ResponseCode(UnsignedShort.valueOf((short) 0x200A), "DeviceProp not supported");
    public static ResponseCode INVALID_OBJECT_FORMAT_CODE = new ResponseCode(UnsignedShort.valueOf((short) 0x200B), "Invalid ObjectFormatCode");
    public static ResponseCode STORE_FULL = new ResponseCode(UnsignedShort.valueOf((short) 0x200C), "Store full");
    public static ResponseCode OBJECT_WRITE_PROTECTED = new ResponseCode(UnsignedShort.valueOf((short) 0x200D), "Object WriteProtected");
    public static ResponseCode STORE_READ_ONLY = new ResponseCode(UnsignedShort.valueOf((short) 0x200E), "Store read-only");
    public static ResponseCode ACCESS_DENIED = new ResponseCode(UnsignedShort.valueOf((short) 0x200F), "Access denied");
    public static ResponseCode NO_THUMBNAIL_PRESENT = new ResponseCode(UnsignedShort.valueOf((short) 0x2010), "No thumbnail present");
    public static ResponseCode SELFTEST_FAILED = new ResponseCode(UnsignedShort.valueOf((short) 0x2011), "SelfTest failed");
    public static ResponseCode PARTIAL_DELETION = new ResponseCode(UnsignedShort.valueOf((short) 0x2012), "Partial deletion");
    public static ResponseCode STORE_NOT_AVAILABLE = new ResponseCode(UnsignedShort.valueOf((short) 0x2013), "Store not available");
    public static ResponseCode SPECIFICATION_BY_FORMAT_UNSUPPORTED = new ResponseCode(UnsignedShort.valueOf((short) 0x2014), "Specification by format unsupported");
    public static ResponseCode NO_VALID_OBJECT_INFO = new ResponseCode(UnsignedShort.valueOf((short) 0x2015), "No valid ObjectInfo");
    public static ResponseCode INVALID_CODE_FORMAT = new ResponseCode(UnsignedShort.valueOf((short) 0x2016), "Invalid Code Format");
    public static ResponseCode UNKNOWN_VENDOR_CODE = new ResponseCode(UnsignedShort.valueOf((short) 0x2017), "Unknown vendor code");
    public static ResponseCode CAPTURE_ALREADY_TERMINATED = new ResponseCode(UnsignedShort.valueOf((short) 0x2018), "Capture already terminated");
    public static ResponseCode DEVICE_BUSY = new ResponseCode(UnsignedShort.valueOf((short) 0x2019), "Device busy");
    public static ResponseCode INVALID_PARENT_OBJECT = new ResponseCode(UnsignedShort.valueOf((short) 0x201A), "Invalid ParentObject");
    public static ResponseCode INVALID_DEVICE_PROP_FORMAT = new ResponseCode(UnsignedShort.valueOf((short) 0x201B), "Invalid DeviceProp format");
    public static ResponseCode INVALID_DEVICE_PROP_VALUE = new ResponseCode(UnsignedShort.valueOf((short) 0x201C), "Invalid DeviceProp value");
    public static ResponseCode INVALID_PARAMETER = new ResponseCode(UnsignedShort.valueOf((short) 0x201D), "Invalid parameter");
    public static ResponseCode SESSION_ALREADY_OPEN = new ResponseCode(UnsignedShort.valueOf((short) 0x201E), "Session already open");
    public static ResponseCode TRANSACTION_CANCELLED = new ResponseCode(UnsignedShort.valueOf((short) 0x201F), "Transaction cancelled");
    public static ResponseCode SPECIFICATION_OF_DESTINATION_UNSUPPORTED = new ResponseCode(UnsignedShort.valueOf((short) 0x2020), "Specification of destination unsupported");
    public static ResponseCode INVALID_ENUM_HANDLE = new ResponseCode(UnsignedShort.valueOf((short) 0x2021), "Invalid EnumHandle");
    public static ResponseCode NO_STREAM_ENABLED = new ResponseCode(UnsignedShort.valueOf((short) 0x2022), "No stream enabled");
    public static ResponseCode INVALID_DATASET = new ResponseCode(UnsignedShort.valueOf((short) 0x2023), "Invalid Dataset");

    /**
     * Returns a response code for the given code.
     *
     * @param code The code to use.
     * @return A {@link ResponseCode} for the given code.
     */
    @SuppressWarnings("java:S1479") // The number of switch cases will be reduced.
    public static ResponseCode valueOf(UnsignedShort code) {
        return switch (code.value()) {
            case 0x2000 -> UNDEFINED;
            case 0x2001 -> OK;
            case 0x2002 -> GENERAL_ERROR;
            case 0x2003 -> SESSION_NOT_OPEN;
            case 0x2004 -> INVALID_TRANSACTION_ID;
            case 0x2005 -> OPERATION_NOT_SUPPORTED;
            case 0x2006 -> PARAMETER_NOT_SUPPORTED;
            case 0x2007 -> INCOMPLETE_TRANSFER;
            case 0x2008 -> INVALID_STORAGE_ID;
            case 0x2009 -> INVALID_OBJECT_HANDLE;
            case 0x200A -> DEVICE_PROP_NOT_SUPPORTED;
            case 0x200B -> INVALID_OBJECT_FORMAT_CODE;
            case 0x200C -> STORE_FULL;
            case 0x200D -> OBJECT_WRITE_PROTECTED;
            case 0x200E -> STORE_READ_ONLY;
            case 0x200F -> ACCESS_DENIED;
            case 0x2010 -> NO_THUMBNAIL_PRESENT;
            case 0x2011 -> SELFTEST_FAILED;
            case 0x2012 -> PARTIAL_DELETION;
            case 0x2013 -> STORE_NOT_AVAILABLE;
            case 0x2014 -> SPECIFICATION_BY_FORMAT_UNSUPPORTED;
            case 0x2015 -> NO_VALID_OBJECT_INFO;
            case 0x2016 -> INVALID_CODE_FORMAT;
            case 0x2017 -> UNKNOWN_VENDOR_CODE;
            case 0x2018 -> CAPTURE_ALREADY_TERMINATED;
            case 0x2019 -> DEVICE_BUSY;
            case 0x201A -> INVALID_PARENT_OBJECT;
            case 0x201B -> INVALID_DEVICE_PROP_FORMAT;
            case 0x201C -> INVALID_DEVICE_PROP_VALUE;
            case 0x201D -> INVALID_PARAMETER;
            case 0x201E -> SESSION_ALREADY_OPEN;
            case 0x201F -> TRANSACTION_CANCELLED;
            case 0x2020 -> SPECIFICATION_OF_DESTINATION_UNSUPPORTED;
            case 0x2021 -> INVALID_ENUM_HANDLE;
            case 0x2022 -> NO_STREAM_ENABLED;
            case 0x2023 -> INVALID_DATASET;
            default -> new ResponseCode(code, "Reserved / Vendor-defined");
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ResponseCode[code='" + String.format("0x%04X", code.value()) + "', description='" + description + "']";
    }

}
