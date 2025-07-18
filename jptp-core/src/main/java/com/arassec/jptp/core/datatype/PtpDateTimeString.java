package com.arassec.jptp.core.datatype;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public record PtpDateTimeString(OffsetDateTime dateTime) {

    private static final DateTimeFormatter dtf = new DateTimeFormatterBuilder()
            .appendPattern("yyyyMMdd'T'HHmmss")
            .optionalStart()
            .appendPattern(".s")
            .optionalEnd()
            .optionalStart()
            .appendPattern("xx") // Zeitzone: Z, +02:00, -07:00
            .optionalEnd()
            .toFormatter();

    public static PtpDateTimeString deserialize(ByteBuffer buffer) {
        PtpString dateTimeString = PtpString.deserialize(buffer);
        if (dateTimeString.rawValue().endsWith("Z") || dateTimeString.rawValue().matches(".*[+-]\\d{4}$")) {
            return new PtpDateTimeString(OffsetDateTime.parse(dateTimeString.rawValue(), dtf));
        } else {
            return new PtpDateTimeString(LocalDateTime.parse(dateTimeString.rawValue(), dtf).atOffset(ZoneOffset.UTC));
        }
    }

}
