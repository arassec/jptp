package com.arassec.jptp.core.datatype;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link PtpDateTime} datatype.
 */
class PtpDateTimeTest {

    /**
     * Tests deserializing.
     */
    @Test
    void testDeserialize() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyyMMdd'T'HHmmss")
                .toFormatter();

        byte[] bytes = formatter.format(now).getBytes(StandardCharsets.UTF_16LE);

        ByteBuffer buffer = ByteBuffer.allocate(bytes.length + 1)
                .order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) bytes.length);
        buffer.put(bytes);
        buffer.rewind();

        PtpDateTime ptpDateTime = PtpDateTime.deserialize(buffer);

        assertThat(ptpDateTime.dateTime()).isEqualTo(
                OffsetDateTime.ofInstant(now.toInstant(ZoneOffset.UTC), ZoneId.of("UTC")).withNano(0)
        );
    }

    /**
     * Tests deserializing with timezone.
     */
    @Test
    void testDeserializeWithTimezone() {
        OffsetDateTime now = OffsetDateTime.now();

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyyMMdd'T'HHmmss.s+0200")
                .toFormatter();

        byte[] bytes = formatter.format(now).getBytes(StandardCharsets.UTF_16LE);

        ByteBuffer buffer = ByteBuffer.allocate(bytes.length + 1)
                .order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) bytes.length);
        buffer.put(bytes);
        buffer.rewind();

        PtpDateTime ptpDateTime = PtpDateTime.deserialize(buffer);

        assertThat(ptpDateTime.dateTime()).isEqualTo(now.withNano(0));
    }

}
