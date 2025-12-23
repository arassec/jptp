package com.arasse.jptp.main.adapter;

import com.arassec.jptp.core.datatype.complex.DeviceInfo;
import com.arassec.jptp.core.datatype.complex.OperationCode;

/**
 * Base class for vendor adapters.
 */
public abstract class BaseVendorAdapter implements VendorAdapter {

    /**
     * Creates a new base vendor adapter.
     */
    protected BaseVendorAdapter() {
    }

    /**
     * Checks whether the selected device supports the required operation.
     *
     * @param deviceInfo    The device's {@link DeviceInfo}.
     * @param operationCode The required {@link OperationCode}.
     * @return {@code true}, if the device supports the operation, {@code false} otherwise.
     */
    protected boolean deviceSupports(DeviceInfo deviceInfo, OperationCode operationCode) {
        return deviceInfo.operationsSupported().stream().anyMatch(operationCode::equals);
    }

}
