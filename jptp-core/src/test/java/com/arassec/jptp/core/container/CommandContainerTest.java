package com.arassec.jptp.core.container;

import com.arassec.jptp.core.datatype.UnsignedInt;
import com.arassec.jptp.core.datatype.valuerange.OperationCode;
import com.arassec.jptp.core.datatype.variable.SessionId;
import com.arassec.jptp.core.datatype.variable.TransactionId;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link CommandContainer}.
 */
public class CommandContainerTest extends BaseContainerTest {

    /**
     * Tests creating instances with the various helper methods and serialization of the resulting
     * command containers.
     */
    @Test
    void testCommandContainer() {
        CommandContainer commandContainer = CommandContainer.newInstance(
                OperationCode.GET_DEVICE_INFO,
                new SessionId(UnsignedInt.valueOf(1)),
                new TransactionId(UnsignedInt.valueOf(1)));

        assertThat(toHex(commandContainer.serialize()))
                .isEqualTo("10 00 00 00 01 00 01 10 01 00 00 00 01 00 00 00");

        commandContainer = CommandContainer.newInstance(OperationCode.INITIATE_CAPTURE,
                new SessionId(UnsignedInt.valueOf(2)),
                new TransactionId(UnsignedInt.valueOf(2)),
                new UnsignedInt(2));

        assertThat(toHex(commandContainer.serialize()))
                .isEqualTo("14 00 00 00 01 00 0E 10 02 00 00 00 02 00 00 00 02 00 00 00");

        commandContainer = CommandContainer.newInstance(OperationCode.OPEN_SESSION,
                new SessionId(UnsignedInt.valueOf(3)),
                new TransactionId(UnsignedInt.valueOf(3)),
                new UnsignedInt(3),
                new UnsignedInt(3));

        assertThat(toHex(commandContainer.serialize()))
                .isEqualTo("18 00 00 00 01 00 02 10 03 00 00 00 03 00 00 00 03 00 00 00 03 00 00 00");

        commandContainer = CommandContainer.newInstance(OperationCode.CLOSE_SESSION,
                new SessionId(UnsignedInt.valueOf(4)),
                new TransactionId(UnsignedInt.valueOf(4)),
                new UnsignedInt(4),
                new UnsignedInt(4),
                new UnsignedInt(4));

        assertThat(toHex(commandContainer.serialize()))
                .isEqualTo("1C 00 00 00 01 00 03 10 04 00 00 00 04 00 00 00 04 00 00 00 04 00 00 00 04 00 00 00");
    }

}
