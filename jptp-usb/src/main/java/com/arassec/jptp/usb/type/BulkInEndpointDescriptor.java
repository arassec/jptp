package com.arassec.jptp.usb.type;

import org.usb4java.EndpointDescriptor;

/**
 * Value object containing a USB {@link EndpointDescriptor} for reading from a device.
 *
 * @param descriptor The USB descriptor for reading data.
 */
public record BulkInEndpointDescriptor(EndpointDescriptor descriptor) {
}
