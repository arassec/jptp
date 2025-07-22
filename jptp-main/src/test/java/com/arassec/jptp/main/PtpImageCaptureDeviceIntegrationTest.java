package com.arassec.jptp.main;

import com.arasse.jptp.main.ImageCaptureDevice;
import com.arasse.jptp.main.PtpImageCaptureDevice;
import com.arassec.jptp.core.datatype.variable.DataObject;
import com.arassec.jptp.usb.UsbPtpDeviceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Disabled("Only for manual tests.")
public class PtpImageCaptureDeviceIntegrationTest {

    @Test
    void testCaptureImage() throws IOException {
        ImageCaptureDevice imageCaptureDevice = new PtpImageCaptureDevice(new UsbPtpDeviceDiscovery());

        if (imageCaptureDevice.initialize()) {
            log.info("Found PTP device: {}", imageCaptureDevice.getDeviceInfo().orElseThrow());
            DataObject dataObject = imageCaptureDevice.captureImage();
            Files.write(Path.of("target/test.jpg"), dataObject.data());
            imageCaptureDevice.teardown();
        }
    }

}
