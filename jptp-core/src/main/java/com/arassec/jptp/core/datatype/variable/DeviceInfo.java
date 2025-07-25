package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.datatype.PtpString;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.valuerange.*;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * The 'Device Info' data set.
 *
 * @param standardVersion           The PTP version.
 * @param vendorExtensionId         The vendor extension ID.
 * @param vendorExtensionVersion    The vendor extension version.
 * @param vendorExtensionDesc       The vendor extension description.
 * @param functionalMode            The device's functional mode.
 * @param operationsSupported       List of supported operations by the device.
 * @param eventsSupported           List of supported events by the device.
 * @param devicePropertiesSupported List of supported device properties.
 * @param captureFormats            List of available capture formats.
 * @param imageFormats              List of available image formats.
 * @param manufacturer              The device's manufacturer.
 * @param model                     The device model.
 * @param deviceVersion             The device version.
 * @param serialNumber              the device's serial number.
 */
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
        SerialNumber serialNumber) implements PtpContainerPayload<DeviceInfo> {

    /**
     * An empty instance for deserialization purposes.
     */
    public static DeviceInfo emptyInstance = new DeviceInfo(null, null, null,
            null, null, null, null, null,
            null, null, null, null, null, null);

    /**
     * {@inheritDoc}
     */
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
                    DevicePropCode.deserializesArray(buffer),
                    ObjectFormatCode.deserializeArray(buffer),
                    ObjectFormatCode.deserializeArray(buffer),
                    new Manufacturer(PtpString.deserialize(buffer)),
                    new Model(PtpString.deserialize(buffer)),
                    new DeviceVersion(PtpString.deserialize(buffer)),
                    new SerialNumber(PtpString.deserialize(buffer))
            );
        }
        return null;
    }

}
