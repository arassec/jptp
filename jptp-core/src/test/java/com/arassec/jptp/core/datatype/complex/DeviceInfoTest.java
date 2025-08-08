package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.datatype.PtpString;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link DeviceInfo}.
 */
class DeviceInfoTest {

    /**
     * Tests deserialization.
     */
    @Test
    void testDeserialize() {
        DeviceInfo expectedDeviceInfo = new DeviceInfo(
                Version.V1_0,
                new VendorExtensionId(UnsignedInt.valueOf(1)),
                new VendorExtensionVersion(UnsignedShort.valueOf((short) 2)),
                new VendorExtensionDescription(new PtpString("desc")),
                FunctionalMode.STANDARD,
                List.of(OperationCode.INITIATE_CAPTURE),
                List.of(EventCode.CAPTURE_COMPLETE),
                List.of(DevicePropCode.valueOf(UnsignedShort.valueOf((short) 3))),
                List.of(ObjectFormatCode.QUICKTIME),
                List.of(ObjectFormatCode.EXIF_JPEG),
                new Manufacturer(new PtpString("manu")),
                new Model(new PtpString("model")),
                new DeviceVersion(new PtpString("101")),
                new SerialNumber(new PtpString("serial"))
        );

        ByteBuffer buffer = ByteBuffer.allocate(256).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(expectedDeviceInfo.standardVersion().version().value());
        buffer.putInt(expectedDeviceInfo.vendorExtensionId().id().value());
        buffer.putShort(expectedDeviceInfo.vendorExtensionVersion().version().value());
        buffer.put((byte) 4); // String length, number of characters in the string!
        buffer.put("desc".getBytes(StandardCharsets.UTF_16LE));
        buffer.putShort(expectedDeviceInfo.functionalMode().mode().value());
        buffer.putInt(1); // Array size
        buffer.putShort(expectedDeviceInfo.operationsSupported().getFirst().code().value());
        buffer.putInt(1); // Array size
        buffer.putShort(expectedDeviceInfo.eventsSupported().getFirst().code().value());
        buffer.putInt(1); // Array size
        buffer.putShort(expectedDeviceInfo.devicePropertiesSupported().getFirst().code().value());
        buffer.putInt(1); // Array size
        buffer.putShort(expectedDeviceInfo.captureFormats().getFirst().code().value());
        buffer.putInt(1); // Array size
        buffer.putShort(expectedDeviceInfo.imageFormats().getFirst().code().value());
        buffer.put((byte) 4); // String length, number of characters in the string!
        buffer.put("manu".getBytes(StandardCharsets.UTF_16LE));
        buffer.put((byte) 5); // String length, number of characters in the string!
        buffer.put("model".getBytes(StandardCharsets.UTF_16LE));
        buffer.put((byte) 3); // String length, number of characters in the string!
        buffer.put("101".getBytes(StandardCharsets.UTF_16LE));
        buffer.put((byte) 6); // String length, number of characters in the string!
        buffer.put("serial".getBytes(StandardCharsets.UTF_16LE));
        buffer.rewind();

        DeviceInfo deviceInfo = DeviceInfo.DESERIALIZER.deserialize(buffer);

        assertThat(deviceInfo).isEqualTo(expectedDeviceInfo);
    }

}
