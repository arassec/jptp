package com.arassec.jptp.core;

import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.EventContainer;
import com.arassec.jptp.core.datatype.variable.CommandResult;
import com.arassec.jptp.core.datatype.variable.SessionId;
import com.arassec.jptp.core.datatype.variable.TransactionId;

/**
 * Defines a PTP device.
 */
public interface PtpDevice {

    /**
     * Initializes the device and has to be called before using it.
     */
    void initialize();

    /**
     * Frees acquired resources and has to be called before shutdown.
     */
    void teardown();

    /**
     * Returns the current PTP session ID of the device.
     *
     * @return The current session ID.
     */
    SessionId getSessionId();

    /**
     * Increments the PTP session ID and returns it.
     *
     * @return The incremented session ID.
     */
    SessionId incrementSessionId();

    /**
     * Increments and returns the current PTP transaction ID.
     *
     * @return The incremented transaction ID.
     */
    TransactionId incrementTransactionId();

    /**
     * Sends a command to the PTP device.
     *
     * @param container       The command container to send.
     * @param payloadInstance An instance of the payload, which will be received in the response to the command.
     * @param <P>             The type of the expected response payload.
     * @return A {@link CommandResult} containing a response container and an optional data container.
     */
    <P extends PtpContainerPayload<P>> CommandResult<P> sendCommand(CommandContainer container, P payloadInstance);

    /**
     * Polls for events from the PTP device.
     *
     * @param payloadInstance An instance of the payload, which will be received in the event.
     * @param <P>             The type of the expected event payload.
     * @return An {@link EventContainer} containing the received data.
     */
    <P extends PtpContainerPayload<P>> EventContainer<P> pollForEvent(P payloadInstance);

}
