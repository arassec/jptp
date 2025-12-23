package com.arasse.jptp.main.adapter;

import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.datatype.complex.DeviceInfo;
import com.arassec.jptp.core.datatype.complex.ResponseCode;

/**
 * Defines an adapter for devices of a specific vendor. This covers specifics of device discovery and image capturing,
 * which can be different from vendor to vendor.
 */
public interface VendorAdapter {

    /**
     * Returns whether this vendor adapter supports the selected device.
     *
     * @param deviceInfo The device info to use.
     * @return {@code true}, if the device is supported by the adapter, {@code false} otherwise.
     */
    boolean supports(DeviceInfo deviceInfo);

    /**
     * Sends the commands required to capture an image.
     *
     * @param ptpDevice The device to use for image capturing.
     * @return The most relevant return code of the operation.
     */
    ResponseCode sendCaptureCommands(PtpDevice ptpDevice);

}
