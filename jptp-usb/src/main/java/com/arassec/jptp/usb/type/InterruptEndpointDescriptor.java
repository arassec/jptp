package com.arassec.jptp.usb.type;

import org.usb4java.EndpointDescriptor;

/**
 * Value object containing a USB {@link EndpointDescriptor} for receiving interrupts from a device.
 *
 * @param descriptor The descriptor to receive interrupts from.
 */
public record InterruptEndpointDescriptor(EndpointDescriptor descriptor) {
}
