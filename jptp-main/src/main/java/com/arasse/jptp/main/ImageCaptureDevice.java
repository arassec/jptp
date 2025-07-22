package com.arasse.jptp.main;

import com.arassec.jptp.core.datatype.variable.DataObject;
import com.arassec.jptp.core.datatype.variable.DeviceInfo;

import java.util.Optional;

/**
 * Defines high-level access to a PTP device for image capturing.
 * <p>
 * Call {@link #initialize()} prior to any usage.
 * Call {@link #teardown()} to release acquired resources prior to shutting down.
 */
public interface ImageCaptureDevice {

    /**
     * Initializes the device manager.
     *
     * @return {@code true}, if the device has been initialized successfully, {@code false} otherwise.
     */
    boolean initialize();

    /**
     * Frees acquired resources.
     */
    void teardown();

    /**
     * Returns the {@link DeviceInfo} of the image capture device.
     *
     * @return The optional {@link DeviceInfo} of the used device if available.
     */
    Optional<DeviceInfo> getDeviceInfo();

    /**
     * Captures an image with the connected device and returns it as payload in the returned {@link DataObject}.
     *
     * @return A PTP {@link DataObject} containing the captured image as byte array in the returned payload.
     */
    DataObject captureImage();

}
