package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.datatype.UnsignedShort;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Value object modelling a PTP device property code.
 *
 * @param code       The code.
 * @param name       The code's name.
 * @param ptpVersion The PTP version introducing the code.
 */
public record DevicePropCode(UnsignedShort code, String name, Version ptpVersion) {

    public static DevicePropCode UNDEFINED = new DevicePropCode(UnsignedShort.valueOf((short) 0x5000), "Undefined", Version.V1_0);
    public static DevicePropCode BATTERY_LEVEL = new DevicePropCode(UnsignedShort.valueOf((short) 0x5001), "BatteryLevel", Version.V1_0);
    public static DevicePropCode FUNCTIONAL_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5002), "FunctionalMode", Version.V1_0);
    public static DevicePropCode IMAGE_SIZE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5003), "ImageSize", Version.V1_0);
    public static DevicePropCode COMPRESSION_SETTING = new DevicePropCode(UnsignedShort.valueOf((short) 0x5004), "CompressionSetting", Version.V1_0);
    public static DevicePropCode WHITE_BALANCE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5005), "WhiteBalance", Version.V1_0);
    public static DevicePropCode RGB_GAIN = new DevicePropCode(UnsignedShort.valueOf((short) 0x5006), "RGBGain", Version.V1_0);
    public static DevicePropCode F_STOP = new DevicePropCode(UnsignedShort.valueOf((short) 0x5007), "FStop", Version.V1_0);
    public static DevicePropCode FOCAL_LENGTH = new DevicePropCode(UnsignedShort.valueOf((short) 0x5008), "FocalLength", Version.V1_0);
    public static DevicePropCode FOCUS_DISTANCE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5009), "FocusDistance", Version.V1_0);
    public static DevicePropCode FOCUS_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x500A), "FocusMode", Version.V1_0);
    public static DevicePropCode EXPOSURE_METERING_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x500B), "ExposureMeteringMode", Version.V1_0);
    public static DevicePropCode FLASH_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x500C), "FlashMode", Version.V1_0);
    public static DevicePropCode EXPOSURE_TIME = new DevicePropCode(UnsignedShort.valueOf((short) 0x500D), "ExposureTime", Version.V1_0);
    public static DevicePropCode EXPOSURE_PROGRAM_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x500E), "ExposureProgramMode", Version.V1_0);
    public static DevicePropCode EXPOSURE_INDEX = new DevicePropCode(UnsignedShort.valueOf((short) 0x500F), "ExposureIndex", Version.V1_0);
    public static DevicePropCode EXPOSURE_BIAS_COMPENSATION = new DevicePropCode(UnsignedShort.valueOf((short) 0x5010), "ExposureBiasCompensation", Version.V1_0);
    public static DevicePropCode DATE_TIME = new DevicePropCode(UnsignedShort.valueOf((short) 0x5011), "DateTime", Version.V1_0);
    public static DevicePropCode CAPTURE_DELAY = new DevicePropCode(UnsignedShort.valueOf((short) 0x5012), "CaptureDelay", Version.V1_0);
    public static DevicePropCode STILL_CAPTURE_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5013), "StillCaptureMode", Version.V1_0);
    public static DevicePropCode CONTRAST = new DevicePropCode(UnsignedShort.valueOf((short) 0x5014), "Contrast", Version.V1_0);
    public static DevicePropCode SHARPNESS = new DevicePropCode(UnsignedShort.valueOf((short) 0x5015), "Sharpness", Version.V1_0);
    public static DevicePropCode DIGITAL_ZOOM = new DevicePropCode(UnsignedShort.valueOf((short) 0x5016), "DigitalZoom", Version.V1_0);
    public static DevicePropCode EFFECT_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5017), "EffectMode", Version.V1_0);
    public static DevicePropCode BURST_NUMBER = new DevicePropCode(UnsignedShort.valueOf((short) 0x5018), "BurstNumber", Version.V1_0);
    public static DevicePropCode BURST_INTERVAL = new DevicePropCode(UnsignedShort.valueOf((short) 0x5019), "BurstInterval", Version.V1_0);
    public static DevicePropCode TIMELAPSE_NUMBER = new DevicePropCode(UnsignedShort.valueOf((short) 0x501A), "TimelapseNumber", Version.V1_0);
    public static DevicePropCode TIMELAPSE_INTERVAL = new DevicePropCode(UnsignedShort.valueOf((short) 0x501B), "TimelapseInterval", Version.V1_0);
    public static DevicePropCode FOCUS_METERING_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x501C), "FocusMeteringMode", Version.V1_0);
    public static DevicePropCode UPLOAD_URL = new DevicePropCode(UnsignedShort.valueOf((short) 0x501D), "UploadURL", Version.V1_0);
    public static DevicePropCode ARTIST = new DevicePropCode(UnsignedShort.valueOf((short) 0x501E), "Artist", Version.V1_0);
    public static DevicePropCode COPYRIGHT_INFO = new DevicePropCode(UnsignedShort.valueOf((short) 0x501F), "CopyrightInfo", Version.V1_0);
    public static DevicePropCode SUPPORTED_STREAMS = new DevicePropCode(UnsignedShort.valueOf((short) 0x5020), "SupportedStreams", Version.V1_1);
    public static DevicePropCode ENABLED_STREAMS = new DevicePropCode(UnsignedShort.valueOf((short) 0x5021), "EnabledStreams", Version.V1_1);
    public static DevicePropCode VIDEO_FORMAT = new DevicePropCode(UnsignedShort.valueOf((short) 0x5022), "VideoFormat", Version.V1_1);
    public static DevicePropCode VIDEO_RESOLUTION = new DevicePropCode(UnsignedShort.valueOf((short) 0x5023), "VideoResolution", Version.V1_1);
    public static DevicePropCode VIDEO_QUALITY = new DevicePropCode(UnsignedShort.valueOf((short) 0x5024), "VideoQuality", Version.V1_1);
    public static DevicePropCode VIDEO_FRAME_RATE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5025), "VideoFramerate", Version.V1_1);
    public static DevicePropCode VIDEO_CONTRAST = new DevicePropCode(UnsignedShort.valueOf((short) 0x5026), "VideoContrast", Version.V1_1);
    public static DevicePropCode VIDEO_BRIGHTNESS = new DevicePropCode(UnsignedShort.valueOf((short) 0x5027), "VideoBrightness", Version.V1_1);
    public static DevicePropCode AUDIO_FORMAT = new DevicePropCode(UnsignedShort.valueOf((short) 0x5028), "AudioFormat", Version.V1_1);
    public static DevicePropCode AUDIO_BITRATE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5029), "AudioBitrate", Version.V1_1);
    public static DevicePropCode AUDIO_SAMPLING_RATE = new DevicePropCode(UnsignedShort.valueOf((short) 0x502A), "AudioSamplingRate", Version.V1_1);
    public static DevicePropCode AUDIO_BIT_PER_SAMPLE = new DevicePropCode(UnsignedShort.valueOf((short) 0x502B), "AudioBitPerSample", Version.V1_1);
    public static DevicePropCode AUDIO_VOLUME = new DevicePropCode(UnsignedShort.valueOf((short) 0x502C), "AudioVolume", Version.V1_1);

    /**
     * Returns an instance for the given code.
     *
     * @param code The code to use.
     * @return A {@link DevicePropCode} instance for the given code.
     */
    @SuppressWarnings("java:S1479") // The number of switch cases will be reduced.
    public static DevicePropCode valueOf(UnsignedShort code) {
        return switch (code.value()) {
            case 0x5000 -> UNDEFINED;
            case 0x5001 -> BATTERY_LEVEL;
            case 0x5002 -> FUNCTIONAL_MODE;
            case 0x5003 -> IMAGE_SIZE;
            case 0x5004 -> COMPRESSION_SETTING;
            case 0x5005 -> WHITE_BALANCE;
            case 0x5006 -> RGB_GAIN;
            case 0x5007 -> F_STOP;
            case 0x5008 -> FOCAL_LENGTH;
            case 0x5009 -> FOCUS_DISTANCE;
            case 0x500A -> FOCUS_MODE;
            case 0x500B -> EXPOSURE_METERING_MODE;
            case 0x500C -> FLASH_MODE;
            case 0x500D -> EXPOSURE_TIME;
            case 0x500E -> EXPOSURE_PROGRAM_MODE;
            case 0x500F -> EXPOSURE_INDEX;
            case 0x5010 -> EXPOSURE_BIAS_COMPENSATION;
            case 0x5011 -> DATE_TIME;
            case 0x5012 -> CAPTURE_DELAY;
            case 0x5013 -> STILL_CAPTURE_MODE;
            case 0x5014 -> CONTRAST;
            case 0x5015 -> SHARPNESS;
            case 0x5016 -> DIGITAL_ZOOM;
            case 0x5017 -> EFFECT_MODE;
            case 0x5018 -> BURST_NUMBER;
            case 0x5019 -> BURST_INTERVAL;
            case 0x501A -> TIMELAPSE_NUMBER;
            case 0x501B -> TIMELAPSE_INTERVAL;
            case 0x501C -> FOCUS_METERING_MODE;
            case 0x501D -> UPLOAD_URL;
            case 0x501E -> ARTIST;
            case 0x501F -> COPYRIGHT_INFO;
            case 0x5020 -> SUPPORTED_STREAMS;
            case 0x5021 -> ENABLED_STREAMS;
            case 0x5022 -> VIDEO_FORMAT;
            case 0x5023 -> VIDEO_RESOLUTION;
            case 0x5024 -> VIDEO_QUALITY;
            case 0x5025 -> VIDEO_FRAME_RATE;
            case 0x5026 -> VIDEO_CONTRAST;
            case 0x5027 -> VIDEO_BRIGHTNESS;
            case 0x5028 -> AUDIO_FORMAT;
            case 0x5029 -> AUDIO_BITRATE;
            case 0x502A -> AUDIO_SAMPLING_RATE;
            case 0x502B -> AUDIO_BIT_PER_SAMPLE;
            case 0x502C -> AUDIO_VOLUME;
            default -> new DevicePropCode(code, "Reserved / Vendor-defined", Version.V1_0);
        };
    }

    /**
     * Deserializes the supplied byte buffer into a List of device property codes.
     *
     * @param buffer The {@link ByteBuffer} containing the PTP array of codes.
     * @return A list of {@link DevicePropCode} instances for the given codes.
     */
    public static List<DevicePropCode> deserializeArray(ByteBuffer buffer) {
        List<DevicePropCode> devicePropCodes = new ArrayList<>();
        int arrayLength = buffer.getInt();
        for (int i = 0; i < arrayLength; i++) {
            devicePropCodes.add(DevicePropCode.valueOf(UnsignedShort.deserialize(buffer)));
        }
        return devicePropCodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DevicePropCode[code='" + String.format("0x%04X", code.value()) + "', name='" + name + "']";
    }

}
