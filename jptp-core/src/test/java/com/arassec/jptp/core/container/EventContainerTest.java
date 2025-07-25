package com.arassec.jptp.core.container;

import com.arassec.jptp.core.datatype.valuerange.ContainerType;
import com.arassec.jptp.core.datatype.valuerange.EventCode;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link EventContainer}.
 */
public class EventContainerTest extends BaseContainerTest {

    /**
     * Tests deserializing an event container.
     */
    @Test
    void testContainerDeserialization() {
        ByteBuffer buffer = ByteBuffer.allocate(512);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(23);
        buffer.putShort(ContainerType.EVENT.getType().value());
        buffer.putShort(EventCode.DEVICE_RESET.code().value());
        buffer.putInt(42);
        buffer.putInt(666);
        buffer.rewind();

        EventContainer<DummyPayload> eventContainer = EventContainer.deserialize(buffer, new DummyPayload());

        assertThat(eventContainer.length().value()).isEqualTo(23);
        assertThat(eventContainer.containerType()).isEqualTo(ContainerType.EVENT);
        assertThat(eventContainer.eventCode()).isEqualTo(EventCode.DEVICE_RESET);
        assertThat(eventContainer.transactionId().id().value()).isEqualTo(42);
        assertThat(eventContainer.payload()).isNotNull();

    }

}
