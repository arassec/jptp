package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public record EventCode(UnsignedShort code, String name) {

    public static EventCode UNDEFINED = new EventCode(UnsignedShort.valueOf((short) 0x4000), "Undefined");
    public static EventCode CANCEL_TRANSACTION = new EventCode(UnsignedShort.valueOf((short) 0x4001), "CancelTransaction");
    public static EventCode OBJECT_ADDED = new EventCode(UnsignedShort.valueOf((short) 0x4002), "ObjectAdded");
    public static EventCode OBJECT_REMOVED = new EventCode(UnsignedShort.valueOf((short) 0x4003), "ObjectRemoved");
    public static EventCode STORE_ADDED = new EventCode(UnsignedShort.valueOf((short) 0x4004), "StoreAdded");
    public static EventCode STORE_REMOVED = new EventCode(UnsignedShort.valueOf((short) 0x4005), "StoreRemoved");
    public static EventCode DEVICE_PROP_CHANGED = new EventCode(UnsignedShort.valueOf((short) 0x4006), "DevicePropChanged");
    public static EventCode OBJECT_INFO_CHANGED = new EventCode(UnsignedShort.valueOf((short) 0x4007), "ObjectInfoChanged");
    public static EventCode DEVICE_INFO_CHANGED = new EventCode(UnsignedShort.valueOf((short) 0x4008), "DeviceInfoChanged");
    public static EventCode REQUEST_OBJECT_TRANSFER = new EventCode(UnsignedShort.valueOf((short) 0x4009), "RequestObjectTransfer");
    public static EventCode STORE_FULL = new EventCode(UnsignedShort.valueOf((short) 0x400A), "StoreFull");
    public static EventCode DEVICE_RESET = new EventCode(UnsignedShort.valueOf((short) 0x400B), "DeviceReset");
    public static EventCode STORAGE_INFO_CHANGED = new EventCode(UnsignedShort.valueOf((short) 0x400C), "StorageInfoChanged");
    public static EventCode CAPTURE_COMPLETE = new EventCode(UnsignedShort.valueOf((short) 0x400D), "CaptureComplete");
    public static EventCode UNREPORTED_STATUS = new EventCode(UnsignedShort.valueOf((short) 0x400E), "UnreportedStatus");

    public static EventCode valueOf(UnsignedShort code) {
        return switch (code.value()) {
            case 0x4000 -> UNDEFINED;
            case 0x4001 -> CANCEL_TRANSACTION;
            case 0x4002 -> OBJECT_ADDED;
            case 0x4003 -> OBJECT_REMOVED;
            case 0x4004 -> STORE_ADDED;
            case 0x4005 -> STORE_REMOVED;
            case 0x4006 -> DEVICE_PROP_CHANGED;
            case 0x4007 -> OBJECT_INFO_CHANGED;
            case 0x4008 -> DEVICE_INFO_CHANGED;
            case 0x4009 -> REQUEST_OBJECT_TRANSFER;
            case 0x400A -> STORE_FULL;
            case 0x400B -> DEVICE_RESET;
            case 0x400C -> STORAGE_INFO_CHANGED;
            case 0x400D -> CAPTURE_COMPLETE;
            case 0x400E -> UNREPORTED_STATUS;
            default -> new EventCode(code, "TODO");
        };
    }

    public static List<EventCode> deserializeArray(ByteBuffer buffer) {
        List<EventCode> eventCodes = new ArrayList<>();
        for (int i = 0; i < buffer.getInt(); i++) {
            eventCodes.add(EventCode.valueOf(UnsignedShort.deserialize(buffer)));
        }
        return eventCodes;
    }

}
