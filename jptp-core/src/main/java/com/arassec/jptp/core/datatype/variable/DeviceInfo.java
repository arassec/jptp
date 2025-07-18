package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.datatype.PtpString;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.valuerange.*;

import java.nio.ByteBuffer;
import java.util.List;

public record DeviceInfo(
        PtpVersion standardVersion,
        VendorExtensionId vendorExtensionId,
        VendorExtensionVersion vendorExtensionVersion,
        VendorExtensionDescription vendorExtensionDesc,
        FunctionalMode functionalMode,
        List<OperationCode> operationsSupported,
        List<EventCode> eventsSupported,
        List<DevicePropCode> devicePropertiesSupported,
        List<ObjectFormatCode> captureFormats,
        List<ObjectFormatCode> imageFormats,
        Manufacturer manufacturer,
        Model model,
        DeviceVersion deviceVersion,
        SerialNumber serialNumber) implements DataContainerPayload<DeviceInfo> {

    public static DeviceInfo emptyInstance = new DeviceInfo(null, null, null,
            null, null, null, null, null,
            null, null, null, null, null, null);

    @Override
    public DeviceInfo deserialize(ByteBuffer buffer) {
        if (buffer.hasRemaining()) {
            return new DeviceInfo(
                    PtpVersion.valueOf(UnsignedShort.deserialize(buffer)),
                    new VendorExtensionId(UnsignedInt.deserialize(buffer)),
                    new VendorExtensionVersion(UnsignedShort.deserialize(buffer)),
                    new VendorExtensionDescription(PtpString.deserialize(buffer)),
                    FunctionalMode.valueOf(UnsignedShort.deserialize(buffer)),
                    OperationCode.deserializeArray(buffer),
                    EventCode.deserializeArray(buffer),
                    DevicePropCode.parseArray(buffer),
                    ObjectFormatCode.parseArray(buffer),
                    ObjectFormatCode.parseArray(buffer),
                    new Manufacturer(PtpString.deserialize(buffer)),
                    new Model(PtpString.deserialize(buffer)),
                    new DeviceVersion(PtpString.deserialize(buffer)),
                    new SerialNumber(PtpString.deserialize(buffer))
            );
        }
        return null;
    }

}
