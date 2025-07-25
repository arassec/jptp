package com.arassec.jptp.main;

import com.arasse.jptp.main.PtpImageCaptureDevice;
import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.PtpDeviceDiscovery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link PtpImageCaptureDevice}.
 */
class PtpImageCaptureDeviceTest {

    /**
     * Tests initialization without finding a PTP device.
     */
    @Test
    void testInitializeNoPtpDevice() {
        PtpDevice ptpDevice = mock(PtpDevice.class);

        PtpDeviceDiscovery deviceDiscovery = mock(PtpDeviceDiscovery.class);
        when(deviceDiscovery.discoverPtpDevices()).thenReturn(List.of(ptpDevice));

        PtpImageCaptureDevice device = new PtpImageCaptureDevice(deviceDiscovery);

        // No DeviceInfo:
        assertThat(device.initialize()).isFalse();
    }

}