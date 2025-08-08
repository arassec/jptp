package com.arassec.jptp.core;

import com.arassec.jptp.core.container.CommandContainer;
import com.arassec.jptp.core.container.EventContainer;
import com.arassec.jptp.core.datatype.complex.CommandResult;
import com.arassec.jptp.core.datatype.complex.SessionId;
import com.arassec.jptp.core.datatype.complex.TransactionId;

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
     * @param container           The command container to send.
     * @param payloadDeserializer A deserializer for the expected payload data.
     * @param <P>                 The type of the expected response payload.
     * @return A {@link CommandResult} containing a response container and an optional data container.
     */
    <P> CommandResult<P> sendCommand(CommandContainer container, PayloadDeserializer<P> payloadDeserializer);

    /**
     * Polls for events from the PTP device.
     *
     * @param payloadDeserializer A deserializer for the event's payload data.
     * @param <P>                 The type of the expected event payload.
     * @return An {@link EventContainer} containing the received data.
     */
    @SuppressWarnings("unused")
    <P> EventContainer<P> pollForEvent(PayloadDeserializer<P> payloadDeserializer);

    /**
     * Sets the default timeout for device operations.
     *
     * @param defaultTimeoutInMillis The timeout to use.
     */
    @SuppressWarnings("unused")
    void setDefaultTimeoutInMillis(long defaultTimeoutInMillis);

    /**
     * Sets the timeout for device event handling.
     *
     * @param eventTimeoutInMillis The timeout to use.
     */
    @SuppressWarnings("unused")
    void setEventTimeoutInMillis(long eventTimeoutInMillis);

}
