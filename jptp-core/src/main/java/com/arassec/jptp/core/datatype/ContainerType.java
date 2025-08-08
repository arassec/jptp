package com.arassec.jptp.core.datatype;

import java.util.Arrays;

/**
 * Defines the types of containers which are used in the communication with PTP devices.
 */
public enum ContainerType {

    /**
     * A command container which contains the necessary data to perform operations on the PTP device.
     */
    COMMAND(UnsignedShort.valueOf((short) 0x0001), "Command"),

    /**
     * A data container which might be returned as part of the response to an operation.
     */
    DATA(UnsignedShort.valueOf((short) 0x0002), "Data"),

    /**
     * A response container which is always returned as part of the response to an operation.
     */
    RESPONSE(UnsignedShort.valueOf((short) 0x0003), "Response"),

    /**
     * An event container containing data send by an PTP device event.
     */
    EVENT(UnsignedShort.valueOf((short) 0x0004), "Event");

    /**
     * The container's type.
     */
    private final UnsignedShort type;

    /**
     * The name of the type.
     */
    private final String name;

    /**
     * Creates a new instance.
     *
     * @param type The type to use.
     * @param name The name to use.
     */
    ContainerType(UnsignedShort type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * Returns an instance for the given type.
     *
     * @param type The container's type.
     * @return A {@link ContainerType} instance for the given type.
     */
    public static ContainerType valueOf(UnsignedShort type) {
        return Arrays.stream(ContainerType.values())
                .filter(v -> v.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ContainerType value: " + type));
    }

    /**
     * Returns the type.
     *
     * @return The type.
     */
    public UnsignedShort getType() {
        return type;
    }

    /**
     * Returns the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

}
