package com.arassec.jptp.core;

import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.ResponseContainer;
import com.arassec.jptp.core.datatype.variable.*;

public interface PtpDevice {

    void initialize();

    void teardown();

    SessionId getSessionId();

    TransactionId getTransactionId();

    CommandResult<DeviceInfo> getDeviceInfo();

    ResponseContainer openSession();

    ResponseContainer closeSession();

    <P extends DataContainerPayload<P>> CommandResult<P> sendCommand(CommandContainer container, P payloadInstance);

}
