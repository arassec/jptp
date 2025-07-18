package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.container.DataContainer;
import com.arassec.jptp.core.container.ResponseContainer;

public record CommandResult<P extends DataContainerPayload<P>>(DataContainer<P> dataContainer, ResponseContainer responseContainer) {
}
