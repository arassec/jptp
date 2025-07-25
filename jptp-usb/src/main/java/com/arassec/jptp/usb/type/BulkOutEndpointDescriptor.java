package com.arassec.jptp.usb.type;

import org.usb4java.EndpointDescriptor;

/**
 * Value object containing a USB {@link EndpointDescriptor} for sending data to a device.
 *
 * @param descriptor The descriptor for writing data to a device.
 */
public record BulkOutEndpointDescriptor(EndpointDescriptor descriptor) {
}
