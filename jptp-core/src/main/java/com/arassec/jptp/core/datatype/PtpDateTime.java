package com.arassec.jptp.core.datatype;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Value object containing a PTP DateTime.
 *
 * @param dateTime The {@link OffsetDateTime} value.
 */
public record PtpDateTime(OffsetDateTime dateTime) {

    /**
     * A {@link DateTimeFormatter} to parse the PTP date time which is received as string.
     */
    private static final DateTimeFormatter dtf = new DateTimeFormatterBuilder()
            .appendPattern("yyyyMMdd'T'HHmmss")
            .optionalStart()
            .appendPattern(".s")
            .optionalEnd()
            .optionalStart()
            .appendPattern("xx") // Zeitzone: Z, +0200, -0700
            .optionalEnd()
            .toFormatter();

    /**
     * Deserializes a PTP date-time value into this value object.
     *
     * @param buffer The {@link ByteBuffer} containing the value.
     * @return A new {@link PtpDateTime} instance with the value.
     */
    public static PtpDateTime deserialize(ByteBuffer buffer) {
        PtpString dateTimeString = PtpString.deserialize(buffer);
        if (dateTimeString.value().endsWith("Z") || dateTimeString.value().matches(".*[+-]\\d{4}$")) {
            return new PtpDateTime(OffsetDateTime.parse(dateTimeString.value(), dtf));
        } else {
            return new PtpDateTime(LocalDateTime.parse(dateTimeString.value(), dtf).atOffset(ZoneOffset.UTC));
        }
    }

}
