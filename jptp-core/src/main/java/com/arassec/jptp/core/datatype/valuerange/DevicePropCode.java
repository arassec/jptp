package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public record DevicePropCode(UnsignedShort code, String name, PtpVersion ptpVersion) {

    public static DevicePropCode UNDEFINED = new DevicePropCode(UnsignedShort.valueOf((short) 0x5000), "Undefined", PtpVersion.V1_0);
    public static DevicePropCode BATTERY_LEVEL = new DevicePropCode(UnsignedShort.valueOf((short) 0x5001), "BatteryLevel", PtpVersion.V1_0);
    public static DevicePropCode FUNCTIONAL_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5002), "FunctionalMode", PtpVersion.V1_0);
    public static DevicePropCode IMAGE_SIZE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5003), "ImageSize", PtpVersion.V1_0);
    public static DevicePropCode COMPRESSION_SETTING = new DevicePropCode(UnsignedShort.valueOf((short) 0x5004), "CompressionSetting", PtpVersion.V1_0);
    public static DevicePropCode WHITE_BALANCE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5005), "WhiteBalance", PtpVersion.V1_0);
    public static DevicePropCode RGB_GAIN = new DevicePropCode(UnsignedShort.valueOf((short) 0x5006), "RGBGain", PtpVersion.V1_0);
    public static DevicePropCode F_STOP = new DevicePropCode(UnsignedShort.valueOf((short) 0x5007), "FStop", PtpVersion.V1_0);
    public static DevicePropCode FOCAL_LENGTH = new DevicePropCode(UnsignedShort.valueOf((short) 0x5008), "FocalLength", PtpVersion.V1_0);
    public static DevicePropCode FOCUS_DISTANCE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5009), "FocusDistance", PtpVersion.V1_0);
    public static DevicePropCode FOCUS_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x500A), "FocusMode", PtpVersion.V1_0);
    public static DevicePropCode EXPOSURE_METERING_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x500B), "ExposureMeteringMode", PtpVersion.V1_0);
    public static DevicePropCode FLASH_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x500C), "FlashMode", PtpVersion.V1_0);
    public static DevicePropCode EXPOSURE_TIME = new DevicePropCode(UnsignedShort.valueOf((short) 0x500D), "ExposureTime", PtpVersion.V1_0);
    public static DevicePropCode EXPOSURE_PROGRAM_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x500E), "ExposureProgramMode", PtpVersion.V1_0);
    public static DevicePropCode EXPOSURE_INDEX = new DevicePropCode(UnsignedShort.valueOf((short) 0x500F), "ExposureIndex", PtpVersion.V1_0);
    public static DevicePropCode EXPOSURE_BIAS_COMPENSATION = new DevicePropCode(UnsignedShort.valueOf((short) 0x5010), "ExposureBiasCompensation", PtpVersion.V1_0);
    public static DevicePropCode DATE_TIME = new DevicePropCode(UnsignedShort.valueOf((short) 0x5011), "DateTime", PtpVersion.V1_0);
    public static DevicePropCode CAPTURE_DELAY = new DevicePropCode(UnsignedShort.valueOf((short) 0x5012), "CaptureDelay", PtpVersion.V1_0);
    public static DevicePropCode STILL_CAPTURE_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5013), "StillCaptureMode", PtpVersion.V1_0);
    public static DevicePropCode CONTRAST = new DevicePropCode(UnsignedShort.valueOf((short) 0x5014), "Contrast", PtpVersion.V1_0);
    public static DevicePropCode SHARPNESS = new DevicePropCode(UnsignedShort.valueOf((short) 0x5015), "Sharpness", PtpVersion.V1_0);
    public static DevicePropCode DIGITAL_ZOOM = new DevicePropCode(UnsignedShort.valueOf((short) 0x5016), "DigitalZoom", PtpVersion.V1_0);
    public static DevicePropCode EFFECT_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5017), "EffectMode", PtpVersion.V1_0);
    public static DevicePropCode BURST_NUMBER = new DevicePropCode(UnsignedShort.valueOf((short) 0x5018), "BurstNumber", PtpVersion.V1_0);
    public static DevicePropCode BURST_INTERVAL = new DevicePropCode(UnsignedShort.valueOf((short) 0x5019), "BurstInterval", PtpVersion.V1_0);
    public static DevicePropCode TIMELAPSE_NUMBER = new DevicePropCode(UnsignedShort.valueOf((short) 0x501A), "TimelapseNumber", PtpVersion.V1_0);
    public static DevicePropCode TIMELAPSE_INTERVAL = new DevicePropCode(UnsignedShort.valueOf((short) 0x501B), "TimelapseInterval", PtpVersion.V1_0);
    public static DevicePropCode FOCUS_METERING_MODE = new DevicePropCode(UnsignedShort.valueOf((short) 0x501C), "FocusMeteringMode", PtpVersion.V1_0);
    public static DevicePropCode UPLOAD_URL = new DevicePropCode(UnsignedShort.valueOf((short) 0x501D), "UploadURL", PtpVersion.V1_0);
    public static DevicePropCode ARTIST = new DevicePropCode(UnsignedShort.valueOf((short) 0x501E), "Artist", PtpVersion.V1_0);
    public static DevicePropCode COPYRIGHT_INFO = new DevicePropCode(UnsignedShort.valueOf((short) 0x501F), "CopyrightInfo", PtpVersion.V1_0);
    public static DevicePropCode SUPPORTED_STREAMS = new DevicePropCode(UnsignedShort.valueOf((short) 0x5020), "SupportedStreams", PtpVersion.V1_1);
    public static DevicePropCode ENABLED_STREAMS = new DevicePropCode(UnsignedShort.valueOf((short) 0x5021), "EnabledStreams", PtpVersion.V1_1);
    public static DevicePropCode VIDEO_FORMAT = new DevicePropCode(UnsignedShort.valueOf((short) 0x5022), "VideoFormat", PtpVersion.V1_1);
    public static DevicePropCode VIDEO_RESOLUTION = new DevicePropCode(UnsignedShort.valueOf((short) 0x5023), "VideoResolution", PtpVersion.V1_1);
    public static DevicePropCode VIDEO_QUALITY = new DevicePropCode(UnsignedShort.valueOf((short) 0x5024), "VideoQuality", PtpVersion.V1_1);
    public static DevicePropCode VIDEO_FRAME_RATE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5025), "VideoFramerate", PtpVersion.V1_1);
    public static DevicePropCode VIDEO_CONTRAST = new DevicePropCode(UnsignedShort.valueOf((short) 0x5026), "VideoContrast", PtpVersion.V1_1);
    public static DevicePropCode VIDEO_BRIGHTNESS = new DevicePropCode(UnsignedShort.valueOf((short) 0x5027), "VideoBrightness", PtpVersion.V1_1);
    public static DevicePropCode AUDIO_FORMAT = new DevicePropCode(UnsignedShort.valueOf((short) 0x5028), "AudioFormat", PtpVersion.V1_1);
    public static DevicePropCode AUDIO_BITRATE = new DevicePropCode(UnsignedShort.valueOf((short) 0x5029), "AudioBitrate", PtpVersion.V1_1);
    public static DevicePropCode AUDIO_SAMPLING_RATE = new DevicePropCode(UnsignedShort.valueOf((short) 0x502A), "AudioSamplingRate", PtpVersion.V1_1);
    public static DevicePropCode AUDIO_BIT_PER_SAMPLE = new DevicePropCode(UnsignedShort.valueOf((short) 0x502B), "AudioBitPerSample", PtpVersion.V1_1);
    public static DevicePropCode AUDIO_VOLUME = new DevicePropCode(UnsignedShort.valueOf((short) 0x502C), "AudioVolume", PtpVersion.V1_1);

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
            default -> new DevicePropCode(code, "TODO", PtpVersion.V1_0);
        };
    }

    public static List<DevicePropCode> parseArray(ByteBuffer buffer) {
        List<DevicePropCode> devicePropCodes = new ArrayList<>();
        for (int i = 0; i < buffer.getInt(); i++) {
            devicePropCodes.add(DevicePropCode.valueOf(UnsignedShort.deserialize(buffer)));
        }
        return devicePropCodes;
    }

}
