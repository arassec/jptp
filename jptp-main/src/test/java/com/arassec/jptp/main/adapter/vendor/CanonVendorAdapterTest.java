package com.arassec.jptp.main.adapter.vendor;

import com.arasse.jptp.main.adapter.vendor.CanonVendorAdapter;
import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.ResponseContainer;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.complex.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CanonVendorAdapterTest {

    private CanonVendorAdapter adapter;

    private final DeviceInfo deviceInfo = mock(DeviceInfo.class);

    private final PtpDevice ptpDevice = mock(PtpDevice.class);

    // Re-creating the private constants from CanonVendorAdapter for testing purposes
    private final OperationCode startCapture = new OperationCode(UnsignedShort.valueOf((short) 0x9128), "Canon-StartImageCapture", Version.V1_0);
    private final OperationCode stopCapture = new OperationCode(UnsignedShort.valueOf((short) 0x9129), "Canon-StopImageCapture", Version.V1_0);

    @BeforeEach
    void setUp() {
        adapter = new CanonVendorAdapter();
    }

    @Test
    void supports_returns_true_if_canon_operations_are_supported() {
        when(deviceInfo.operationsSupported()).thenReturn(List.of(
                startCapture,
                stopCapture,
                OperationCode.GET_OBJECT_HANDLES,
                OperationCode.GET_OBJECT
        ));

        assertThat(adapter.supports(deviceInfo)).isTrue();
    }

    @Test
    void supports_returns_false_if_canon_operation_is_missing() {
        when(deviceInfo.operationsSupported()).thenReturn(List.of(
                startCapture,
                // stopCapture missing
                OperationCode.GET_OBJECT_HANDLES,
                OperationCode.GET_OBJECT
        ));

        assertThat(adapter.supports(deviceInfo)).isFalse();
    }

    @Test
    @SuppressWarnings("unchecked")
    void sendCaptureCommands_sends_sequence_successfully() {
        CommandResult<Object> commandResult = mock(CommandResult.class);
        ResponseContainer responseContainer = mock(ResponseContainer.class);

        when(ptpDevice.incrementTransactionId()).thenReturn(new TransactionId(UnsignedInt.zeroInstance()));
        when(ptpDevice.sendCommand(any(CommandContainer.class), any())).thenReturn(commandResult);
        when(commandResult.responseContainer()).thenReturn(responseContainer);
        when(responseContainer.responseCode()).thenReturn(ResponseCode.OK);

        ResponseCode result = adapter.sendCaptureCommands(ptpDevice);

        assertThat(result).isEqualTo(ResponseCode.OK);

        // Verify 4 commands sent (Start/Focus, Start/Release, Stop/Release, Stop/Focus)
        verify(ptpDevice, times(4)).sendCommand(any(CommandContainer.class), any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void sendCaptureCommands_aborts_on_error() {
        CommandResult<Object> successResult = mock(CommandResult.class);
        ResponseContainer successResponse = mock(ResponseContainer.class);
        when(successResult.responseContainer()).thenReturn(successResponse);
        when(successResponse.responseCode()).thenReturn(ResponseCode.OK);

        CommandResult<Object> errorResult = mock(CommandResult.class);
        ResponseContainer errorResponse = mock(ResponseContainer.class);
        when(errorResult.responseContainer()).thenReturn(errorResponse);
        when(errorResponse.responseCode()).thenReturn(ResponseCode.GENERAL_ERROR);

        when(ptpDevice.incrementTransactionId()).thenReturn(new TransactionId(UnsignedInt.zeroInstance()));

        // First call succeeds, second fails
        when(ptpDevice.sendCommand(any(CommandContainer.class), any()))
                .thenReturn(successResult)
                .thenReturn(errorResult);

        ResponseCode result = adapter.sendCaptureCommands(ptpDevice);

        assertThat(result).isEqualTo(ResponseCode.GENERAL_ERROR);

        // Verify only 2 commands sent because of early exit
        verify(ptpDevice, times(2)).sendCommand(any(CommandContainer.class), any());
    }
}

