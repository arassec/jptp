package com.arassec.jptp.main;

import com.arasse.jptp.main.ImageCaptureDevice;
import com.arasse.jptp.main.PtpImageCaptureDevice;
import com.arassec.jptp.core.datatype.complex.DataObject;
import com.arassec.jptp.usb.UsbPtpDeviceDiscovery;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test of the {@link ImageCaptureDevice} for manual use.
 */
@Disabled("Only for manual tests.")
class PtpImageCaptureDeviceIntegrationTest {

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
    void testCaptureImage() throws IOException, InterruptedException {
        Path testImage = Path.of("target/test.jpg");

        Files.deleteIfExists(testImage);

        ImageCaptureDevice imageCaptureDevice = new PtpImageCaptureDevice(new UsbPtpDeviceDiscovery());

        // Execute two times to test LibUsb context renewal.
        for (int i = 0; i < 36; i++) {
            captureImage(imageCaptureDevice);
            Thread.sleep(250);
            log.info("Captured image {}", i + 1);
        }

        assertThat(Files.exists(testImage)).isTrue();
    }

    /**
     * Captures an image.
     *
     * @param imageCaptureDevice The device to capture the image.
     * @throws IOException In case of errors.
     */
    private void captureImage(ImageCaptureDevice imageCaptureDevice) throws IOException {
        if (imageCaptureDevice.initialize(Duration.ofSeconds(5), Duration.ofSeconds(5))) {
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
