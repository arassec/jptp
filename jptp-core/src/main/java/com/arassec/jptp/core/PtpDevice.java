package com.arassec.jptp.core;

import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.EventContainer;
import com.arassec.jptp.core.datatype.variable.*;

public interface PtpDevice {

    void initialize();

    void teardown();

    SessionId getSessionId();

    SessionId incrementSessionId();

    TransactionId incrementTransactionId();

    <P extends PtpContainerPayload<P>> CommandResult<P> sendCommand(CommandContainer container, P payloadInstance);

    <P extends PtpContainerPayload<P>> EventContainer<P> pollForEvent(P payloadInstance);

}
