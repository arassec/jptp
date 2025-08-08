package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.container.DataContainer;
import com.arassec.jptp.core.container.ResponseContainer;

import javax.annotation.processing.Generated;

/**
 * Command result containing data and response.
 *
 * @param dataContainer     Optional data container.
 * @param responseContainer Response container.
 * @param <P>               Type of the data container's payload.
 */
@Generated("human")
public record CommandResult<P>(DataContainer<P> dataContainer,
                               ResponseContainer responseContainer) {
}
