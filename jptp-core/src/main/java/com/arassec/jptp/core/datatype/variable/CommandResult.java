package com.arassec.jptp.core.datatype.variable;

import com.arassec.jptp.core.PtpContainerPayload;
import com.arassec.jptp.core.container.DataContainer;
import com.arassec.jptp.core.container.ResponseContainer;

/**
 * Command result containing data and response.
 *
 * @param dataContainer     Optional data container.
 * @param responseContainer Response container.
 * @param <P>               Type of the data container's payload.
 */
public record CommandResult<P extends PtpContainerPayload<P>>(DataContainer<P> dataContainer,
                                                              ResponseContainer responseContainer) {
}
