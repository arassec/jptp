package com.arassec.jptp.usb.event;

import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class EventHandlingThread extends Thread {

    private volatile boolean abort = false;

    public void abort() {
        abort = true;
    }

    @Override
    public void run() {
        while (!abort) {
            int result = LibUsb.handleEventsTimeout(null, 15000);
            if (result != LibUsb.SUCCESS) {
                throw new LibUsbException("Unable to handle event", result);
            }
        }
    }

}
