package com.arassec.jptp.main;

import com.arasse.jptp.main.ImageCaptureDevice;
import com.arasse.jptp.main.PtpImageCaptureDevice;
import com.arassec.jptp.core.datatype.variable.DataObject;
import com.arassec.jptp.usb.UsbPtpDeviceDiscovery;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Integration test of the {@link ImageCaptureDevice} for manual use.
 */
@Disabled("Only for manual tests.")
public class PtpImageCaptureDeviceIntegrationTest {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(PtpImageCaptureDeviceIntegrationTest.class);

    /**
     * Tests capturing an image with a USB connected camera.
     *
     * @throws IOException In case of errors.
     */
    @Test
    void testCaptureImage() throws IOException {
        ImageCaptureDevice imageCaptureDevice = new PtpImageCaptureDevice(new UsbPtpDeviceDiscovery());

        // Execute two times to test LibUsb context renewal.
        captureImage(imageCaptureDevice);
        captureImage(imageCaptureDevice);
    }

    private void captureImage(ImageCaptureDevice imageCaptureDevice) throws IOException {
        if (imageCaptureDevice.initialize()) {
            Optional<DataObject> optionalDataObject = imageCaptureDevice.captureImage();
            if (optionalDataObject.isPresent()) {
                Files.write(Path.of("target/test.jpg"), optionalDataObject.get().data());
                log.info("Capture image successfully");
            } else {
                log.info("Capture image failed");
            }
            imageCaptureDevice.teardown();
        } else {
            log.error("No PTP device found!");
        }
    }

}
