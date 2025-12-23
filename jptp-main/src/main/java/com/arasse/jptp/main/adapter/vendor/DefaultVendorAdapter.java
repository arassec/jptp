package com.arasse.jptp.main.adapter.vendor;

import com.arasse.jptp.main.adapter.BaseVendorAdapter;
import com.arassec.jptp.core.PtpDevice;
import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.ResponseContainer;
import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.complex.DeviceInfo;
import com.arassec.jptp.core.datatype.complex.OperationCode;
import com.arassec.jptp.core.datatype.complex.ResponseCode;

/**
 * The default vendor adapter handles devices without custom mechanisms or vendor specific operation codes. This works
 * with the tested Nikon cameras.
 */
public class DefaultVendorAdapter extends BaseVendorAdapter {

    /**
     * Creates a new default vendor adapter.
     */
    public DefaultVendorAdapter() {
        // Make JavaDoc-Plugin happy.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(DeviceInfo deviceInfo) {
        return deviceSupports(deviceInfo, OperationCode.INITIATE_CAPTURE)
                && deviceSupports(deviceInfo, OperationCode.GET_OBJECT_HANDLES)
                && deviceSupports(deviceInfo, OperationCode.GET_OBJECT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseCode sendCaptureCommands(PtpDevice ptpDevice) {
        ResponseContainer responseContainer = ptpDevice.sendCommand(CommandContainer.newInstance(OperationCode.INITIATE_CAPTURE,
                        null, ptpDevice.incrementTransactionId(), UnsignedInt.zeroInstance(), UnsignedInt.zeroInstance()), null)
                .responseContainer();
        return responseContainer.responseCode();
    }

}
