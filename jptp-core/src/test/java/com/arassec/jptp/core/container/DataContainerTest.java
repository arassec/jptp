package com.arassec.jptp.core.container;

import com.arassec.jptp.core.datatype.ContainerType;
import com.arassec.jptp.core.datatype.complex.OperationCode;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link DataContainer}.
 */
class DataContainerTest extends BaseContainerTest {

    /**
     * Tests deserializing a data container.
     */
    @Test
    void testDeserialization() {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(23);
        buffer.putShort(ContainerType.DATA.getType().value());
        buffer.putShort(OperationCode.GET_DEVICE_INFO.code().value());
        buffer.putInt(42);
        buffer.putInt(666);
        buffer.rewind();

        DataContainer<DummyPayload> dataContainer = DataContainer.deserialize(buffer, new DummyPayload());

        assertThat(dataContainer.length().value()).isEqualTo(23);
        assertThat(dataContainer.containerType()).isEqualTo(ContainerType.DATA);
        assertThat(dataContainer.operationCode()).isEqualTo(OperationCode.GET_DEVICE_INFO);
        assertThat(dataContainer.transactionId().id().value()).isEqualTo(42);
        assertThat(dataContainer.payload()).isNotNull();
    }

}
