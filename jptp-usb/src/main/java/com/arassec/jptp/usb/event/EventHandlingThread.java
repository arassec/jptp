package com.arassec.jptp.usb.event;

import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

/**
 * Thread that enables USB event handling.
 */
public class EventHandlingThread extends Thread {

    /**
     * Indicates whether to continue event handling or to abort it.
     */
    private volatile boolean abort = false;

    /**
     * Creates a new instance.
     */
    public EventHandlingThread() {
    }

    /**
     * Aborts the event handling thread.
     */
    public void abort() {
        abort = true;
    }

    /**
     * Polls for USB events until aborted.
     */
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
