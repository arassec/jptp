package com.arasse.jptp.main.adapter.vendor;

import com.arasse.jptp.main.adapter.BaseVendorAdapter;
import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.UnsignedShort;
import com.arassec.jptp.core.datatype.complex.*;

/**
 * Vendor adapter for Canon cameras. Up to now only tested with Canon EOS 500D.
 * Operation codes have been documented by
 * <a href="https://julianschroden.com/post/2023-06-15-capturing-images-using-ptp-ip-on-canon-eos-cameras/">Julian Schroden</a>.
 */
public class CanonVendorAdapter extends BaseVendorAdapter {

    /**
     * Operation code for starting image capture on Canon devices.
     */
    private static final OperationCode START_IMAGE_CAPTURE = new OperationCode(UnsignedShort.valueOf((short) 0x9128), "Canon-StartImageCapture", Version.V1_0);

    /**
     * Operation code for stopping image capture on Canon devices.
     */
    private static final OperationCode STOP_IMAGE_CAPTURE = new OperationCode(UnsignedShort.valueOf((short) 0x9129), "Canon-StopImageCapture", Version.V1_0);

    /**
     * Parameter for commands: 0x00000000.
     */
    private static final UnsignedInt PARAM_ZERO = UnsignedInt.valueOf(0x0);

    /**
     * Parameter for focus action: 0x00000001.
     */
    private static final UnsignedInt PARAM_FOCUS = UnsignedInt.valueOf(0x01);

    /**
     * Parameter for release action: 0x00000002.
     */
    private static final UnsignedInt PARAM_RELEASE = UnsignedInt.valueOf(0x02);

    /**
     * Creates a new Canon vendor adapter.
     */
    public CanonVendorAdapter() {
        // Make JavaDoc-Plugin happy.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(DeviceInfo deviceInfo) {
        return deviceSupports(deviceInfo, START_IMAGE_CAPTURE)
                && deviceSupports(deviceInfo, STOP_IMAGE_CAPTURE)
                && deviceSupports(deviceInfo, OperationCode.GET_OBJECT_HANDLES)
                && deviceSupports(deviceInfo, OperationCode.GET_OBJECT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseCode sendCaptureCommands(PtpDevice ptpDevice) {

        CommandResult<Object> commandResult = ptpDevice.sendCommand(CommandContainer.newInstance(START_IMAGE_CAPTURE,
                null, ptpDevice.incrementTransactionId(), PARAM_FOCUS, PARAM_ZERO), null);
        if (!ResponseCode.OK.equals(commandResult.responseContainer().responseCode())) {
            return commandResult.responseContainer().responseCode();
        }

        commandResult = ptpDevice.sendCommand(CommandContainer.newInstance(START_IMAGE_CAPTURE,
                null, ptpDevice.incrementTransactionId(), PARAM_RELEASE), null);
        if (!ResponseCode.OK.equals(commandResult.responseContainer().responseCode())) {
            return commandResult.responseContainer().responseCode();
        }

        commandResult = ptpDevice.sendCommand(CommandContainer.newInstance(STOP_IMAGE_CAPTURE,
                null, ptpDevice.incrementTransactionId(), PARAM_RELEASE), null);
        if (!ResponseCode.OK.equals(commandResult.responseContainer().responseCode())) {
            return commandResult.responseContainer().responseCode();
        }

        commandResult = ptpDevice.sendCommand(CommandContainer.newInstance(STOP_IMAGE_CAPTURE,
                null, ptpDevice.incrementTransactionId(), PARAM_FOCUS), null);

        return commandResult.responseContainer().responseCode();
    }

}
