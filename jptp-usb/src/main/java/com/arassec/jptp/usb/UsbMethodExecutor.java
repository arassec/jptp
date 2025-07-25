package com.arassec.jptp.usb;

/**
 * Provides a method to use as callback for LibUSB command executions.
 */
public interface UsbMethodExecutor {

    /**
     * Must return the LibUSB command's return code.
     *
     * @return The return code of the executed LibUSB command.
     */
    int execute();

}
