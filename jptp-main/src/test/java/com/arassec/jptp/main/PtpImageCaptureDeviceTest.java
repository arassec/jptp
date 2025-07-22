package com.arassec.jptp.main;

import com.arasse.jptp.main.PtpImageCaptureDevice;
import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.PtpDeviceDiscovery;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.DataContainer;
import com.arassec.jptp.core.datatype.valuerange.OperationCode;
import com.arassec.jptp.core.datatype.valuerange.ResponseCode;
import com.arassec.jptp.core.datatype.variable.CommandResult;
import com.arassec.jptp.core.datatype.variable.DeviceInfo;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PtpImageCaptureDeviceTest {

    @Test
    void testInitialize() {
        PtpDevice ptpDevice = mock(PtpDevice.class);

        PtpDeviceDiscovery deviceDiscovery = mock(PtpDeviceDiscovery.class);
        when(deviceDiscovery.discoverPtpDevices()).thenReturn(List.of(ptpDevice));

        PtpImageCaptureDevice device = new PtpImageCaptureDevice(deviceDiscovery);

        // No DeviceInfo:
        assertThat(device.initialize()).isFalse();

        DeviceInfo deviceInfo = mock(DeviceInfo.class);

        CommandResult<DeviceInfo> commandResult = mock(CommandResult.class, Answers.RETURNS_DEEP_STUBS);
        when(commandResult.responseContainer().responseCode()).thenReturn(ResponseCode.OK);
        when(commandResult.dataContainer().payload()).thenReturn(deviceInfo);

        when(ptpDevice.sendCommand(CommandContainer.newInstance(OperationCode.GET_DEVICE_INFO, ptpDevice.getSessionId(), ptpDevice.incrementTransactionId(), null, null, null),
                DeviceInfo.emptyInstance)).thenReturn(commandResult);

        // Required operations unsupported:
        assertThat(device.initialize()).isFalse();
    }

}