package com.arassec.jptp.core.container;

import com.arassec.jptp.core.datatype.valuerange.ContainerType;
import com.arassec.jptp.core.datatype.valuerange.EventCode;
import com.arassec.jptp.core.datatype.valuerange.ResponseCode;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link ResponseContainer}.
 */
public class ResponseContainerTest extends BaseContainerTest {

    /**
     * Tests deserializing a response container.
     */
    @Test
    void testContainerDeserialization() {
        ByteBuffer buffer = ByteBuffer.allocate(512);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(23);
        buffer.putShort(ContainerType.RESPONSE.getType().value());
        buffer.putShort(ResponseCode.ACCESS_DENIED.code().value());
        buffer.putInt(42);
        buffer.putInt(666);
        buffer.rewind();

        ResponseContainer responseContainer = ResponseContainer.deserialize(buffer);

        assertThat(responseContainer.length().value()).isEqualTo(23);
        assertThat(responseContainer.containerType()).isEqualTo(ContainerType.RESPONSE);
        assertThat(responseContainer.responseCode()).isEqualTo(ResponseCode.ACCESS_DENIED);
        assertThat(responseContainer.transactionId().id().value()).isEqualTo(42);
        assertThat(responseContainer.sessionId().id().value()).isEqualTo(666);
    }

}
