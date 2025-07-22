package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.container.DataContainer;
import com.arassec.jptp.core.container.ResponseContainer;

public record CommandResult<P extends PtpContainerPayload<P>>(DataContainer<P> dataContainer, ResponseContainer responseContainer) {
}
