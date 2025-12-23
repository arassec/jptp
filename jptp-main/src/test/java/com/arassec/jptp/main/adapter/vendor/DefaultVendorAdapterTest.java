package com.arassec.jptp.main.adapter.vendor;

import com.arasse.jptp.main.adapter.vendor.DefaultVendorAdapter;
import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.ResponseContainer;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.complex.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultVendorAdapterTest {

    private DefaultVendorAdapter adapter;

    private final DeviceInfo deviceInfo = mock(DeviceInfo.class);

    private final PtpDevice ptpDevice = mock(PtpDevice.class);

    @BeforeEach
    void setUp() {
        adapter = new DefaultVendorAdapter();
    }

    @Test
    void supports_returns_true_if_required_operations_are_supported() {
        when(deviceInfo.operationsSupported()).thenReturn(List.of(
                OperationCode.INITIATE_CAPTURE,
                OperationCode.GET_OBJECT_HANDLES,
                OperationCode.GET_OBJECT
        ));

        assertThat(adapter.supports(deviceInfo)).isTrue();
    }

    @Test
    void supports_returns_false_if_operation_is_missing() {
        when(deviceInfo.operationsSupported()).thenReturn(List.of(
                OperationCode.INITIATE_CAPTURE,
                OperationCode.GET_OBJECT_HANDLES
                // GET_OBJECT missing
        ));

        assertThat(adapter.supports(deviceInfo)).isFalse();
    }

    @Test
    @SuppressWarnings("unchecked")
    void sendCaptureCommands_sends_initiate_capture_command() {
        CommandResult<Object> commandResult = mock(CommandResult.class);
        ResponseContainer responseContainer = mock(ResponseContainer.class);

        when(ptpDevice.incrementTransactionId()).thenReturn(new TransactionId(UnsignedInt.zeroInstance()));
        when(ptpDevice.sendCommand(any(CommandContainer.class), any())).thenReturn(commandResult);
        when(commandResult.responseContainer()).thenReturn(responseContainer);
        when(responseContainer.responseCode()).thenReturn(ResponseCode.OK);

        ResponseCode result = adapter.sendCaptureCommands(ptpDevice);

        assertThat(result).isEqualTo(ResponseCode.OK);
        verify(ptpDevice).sendCommand(any(CommandContainer.class), any());
    }
}

