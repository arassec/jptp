package com.arassec.jptp.main;

import com.arasse.jptp.main.PtpImageCaptureDevice;
import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.PtpDeviceDiscovery;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.DataContainer;
import com.arassec.jptp.core.container.ResponseContainer;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.valuerange.OperationCode;
import com.arassec.jptp.core.datatype.valuerange.PtpVersion;
import com.arassec.jptp.core.datatype.valuerange.ResponseCode;
import com.arassec.jptp.core.datatype.variable.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Tests the {@link PtpImageCaptureDevice}.
 */
class PtpImageCaptureDeviceTest {

    /**
     * Mocked PTP device.
     */
    private PtpDevice ptpDeviceMock;

    /**
     * Mocked image capture device.
     */
    private PtpImageCaptureDevice imageCaptureDevice;

    @BeforeEach
    void initialize() {
        ptpDeviceMock = mock(PtpDevice.class);

        mockPtpDevice(List.of(
                OperationCode.INITIATE_CAPTURE,
                OperationCode.GET_OBJECT_HANDLES,
                OperationCode.GET_OBJECT));

        PtpDeviceDiscovery deviceDiscovery = mock(PtpDeviceDiscovery.class);
        when(deviceDiscovery.discoverPtpDevices()).thenReturn(List.of(ptpDeviceMock));

        imageCaptureDevice = new PtpImageCaptureDevice(deviceDiscovery);
    }

    /**
     * Tests initialization without finding a PTP device.
     */
    @Test
    void testInitializeNoPtpDevice() {
        ptpDeviceMock = mock(PtpDevice.class);

        PtpDeviceDiscovery deviceDiscovery = mock(PtpDeviceDiscovery.class);
        when(deviceDiscovery.discoverPtpDevices()).thenReturn(List.of(ptpDeviceMock));

        PtpImageCaptureDevice device = new PtpImageCaptureDevice(deviceDiscovery);

        // No DeviceInfo:
        assertThat(device.initialize()).isFalse();
    }

    /**
     * Tests initialization with a PTP valid device.
     */
    @Test
    void testInitializeWithPtpDevice() {
        assertThat(imageCaptureDevice.initialize()).isTrue();
        assertThat(imageCaptureDevice.isInitialized()).isTrue();
    }

    /**
     * Tests initialization with a valid PTP device.
     */
    @Test
    void testInitializeOpenSessionFails() {
        ResponseContainer openSessionResponseContainer = mock(ResponseContainer.class);
        when(openSessionResponseContainer.responseCode()).thenReturn(ResponseCode.SESSION_NOT_OPEN);

        CommandResult<NoData> openSessionCommandResult = new CommandResult<>(null, openSessionResponseContainer);

        mockCommandHandling(OperationCode.OPEN_SESSION, openSessionCommandResult);

        // OpenSession command fails:
        assertThrows(IllegalStateException.class, imageCaptureDevice::initialize);
    }

    /**
     * Tests tearing down.
     */
    @Test
    void testTeardown() {
        // Should do no harm:
        assertDoesNotThrow(imageCaptureDevice::teardown);

        // Initialize the device:
        assertThat(imageCaptureDevice.initialize()).isTrue();
        assertThat(imageCaptureDevice.isInitialized()).isTrue();

        // Teardown fails:
        mockCommandHandling(OperationCode.CLOSE_SESSION, new CommandResult<NoData>(null, new ResponseContainer(null, null, ResponseCode.ACCESS_DENIED, null, null)));
        assertThrows(IllegalStateException.class, imageCaptureDevice::teardown);

        // Teardown succeeds:
        mockCommandHandling(OperationCode.CLOSE_SESSION, new CommandResult<NoData>(null, new ResponseContainer(null, null, ResponseCode.OK, null, null)));
        assertDoesNotThrow(imageCaptureDevice::teardown);
        assertThat(imageCaptureDevice.isInitialized()).isFalse();
    }

    /**
     * Tests capturing an image.
     */
    @Test
    void testCaptureImage() {
        // Uninitialized -> Exception
        assertThrows(IllegalStateException.class, imageCaptureDevice::captureImage);

        assertThat(imageCaptureDevice.initialize()).isTrue();

        // Now mock the hole thing...
        CommandContainer expectedCommandContainer = CommandContainer.newInstance(OperationCode.INITIATE_CAPTURE, null, null, UnsignedInt.zeroInstance(), UnsignedInt.zeroInstance());
        when(ptpDeviceMock.sendCommand(eq(expectedCommandContainer), ArgumentMatchers.<NoData>any()))
                .thenReturn(new CommandResult<>(null,
                        new ResponseContainer(null, null, ResponseCode.OK, null, null)));

        AtomicInteger counter = new AtomicInteger(0);
        when(ptpDeviceMock.sendCommand(eq(CommandContainer.newInstance(OperationCode.GET_OBJECT_HANDLES, null, null, UnsignedInt.maxInstance())), eq(ObjectHandleArray.emptyInstance)))
                .thenAnswer(invocation -> switch (counter.incrementAndGet()) {
                    case 0, 1 -> new CommandResult<>(
                            new DataContainer<>(null, null, null, null, new ObjectHandleArray(new ArrayList<>(List.of(
                                    new ObjectHandle(UnsignedInt.valueOf(1)))))),
                            new ResponseContainer(null, null, ResponseCode.OK, null, null)
                    );
                    default -> new CommandResult<>(
                            new DataContainer<>(null, null, null, null, new ObjectHandleArray(new ArrayList<>(List.of(
                                    new ObjectHandle(UnsignedInt.valueOf(1)), new ObjectHandle(UnsignedInt.valueOf(2)))))),
                            new ResponseContainer(null, null, ResponseCode.OK, null, null)
                    );
                });

        expectedCommandContainer = CommandContainer.newInstance(OperationCode.GET_OBJECT, null, null, UnsignedInt.valueOf(2));
        when(ptpDeviceMock.sendCommand(eq(expectedCommandContainer), ArgumentMatchers.<DataObject>any()))
                .thenReturn(new CommandResult<>(
                        new DataContainer<>(null, null, null, null, new DataObject(new byte[]{1, 2, 3, 4})),
                        new ResponseContainer(null, null, ResponseCode.OK, null, null)
                ));

        expectedCommandContainer = CommandContainer.newInstance(OperationCode.DELETE_OBJECT, null, null, UnsignedInt.valueOf(2));
        when(ptpDeviceMock.sendCommand(eq(expectedCommandContainer), ArgumentMatchers.<NoData>any()))
                .thenReturn(new CommandResult<>(
                        null,
                        new ResponseContainer(null, null, ResponseCode.OK, null, null)
                ));

        // This should finally work now:
        Optional<DataObject> dataObject = imageCaptureDevice.captureImage();

        assertThat(dataObject).isPresent();
        assertThat(dataObject.get().data()).isEqualTo(new byte[]{1, 2, 3, 4});
    }

    /**
     * Mocks a {@link PtpDevice}.
     *
     * @param operationCodes The operation codes supported by the mocked device.
     */
    private void mockPtpDevice(List<OperationCode> operationCodes) {
        DataContainer<DeviceInfo> deviceInfoDataContainer = new DataContainer<>(null, null, null, null,
                new DeviceInfo(
                        PtpVersion.V1_0, null, null, null,
                        null, operationCodes, null, null, null,
                        null, null, null, null, null
                ));

        ResponseContainer deviceResponseContainer = mock(ResponseContainer.class);
        when(deviceResponseContainer.responseCode()).thenReturn(ResponseCode.OK);

        CommandResult<DeviceInfo> deviceInfoCommandResult = new CommandResult<>(deviceInfoDataContainer, deviceResponseContainer);

        ResponseContainer openSessionResponseContainer = mock(ResponseContainer.class);
        when(openSessionResponseContainer.responseCode()).thenReturn(ResponseCode.OK);

        CommandResult<NoData> openSessionCommandResult = new CommandResult<>(null, openSessionResponseContainer);

        mockCommandHandling(OperationCode.GET_DEVICE_INFO, deviceInfoCommandResult);
        mockCommandHandling(OperationCode.OPEN_SESSION, openSessionCommandResult);
    }

    private <P extends PtpContainerPayload<P>> void mockCommandHandling(OperationCode operationCode, CommandResult<P> commandResult) {
        CommandContainer expectedCommandContainer = CommandContainer.newInstance(operationCode, null, null);
        when(ptpDeviceMock.sendCommand(eq(expectedCommandContainer), ArgumentMatchers.<P>any())).thenReturn(commandResult);
    }

}