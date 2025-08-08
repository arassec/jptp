package com.arassec.jptp.core.container;

import com.arassec.jptp.core.PayloadDeserializer;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Base class for container tests with common functionality.
 */
public abstract class BaseContainerTest {

    /**
     * Converts the supplied byte buffer into a string with hex values for better comparison.
     *
     * @param buffer The {@link ByteBuffer} to convert.
     * @return A string with the byte buffer's values as hex values.
     */
    protected String toHex(ByteBuffer buffer) {
        StringBuilder sb = new StringBuilder();
        for (int i = buffer.position(); i < buffer.limit(); i++) {
            sb.append(String.format("%02X ", buffer.get(i)));
        }
        return sb.toString().trim();
    }

    /**
     * Container payload for testing.
     */
    protected static class DummyPayload implements PayloadDeserializer<DummyPayload> {

        /**
         * {@inheritDoc}
         */
        @Override
        public DummyPayload deserialize(ByteBuffer buffer) {
            assertThat(buffer.getInt()).isEqualTo(666);
            return new DummyPayload();
        }

    }

}
