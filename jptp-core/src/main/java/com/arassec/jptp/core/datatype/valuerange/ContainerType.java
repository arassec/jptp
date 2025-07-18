package com.arassec.jptp.core.datatype.valuerange;

import com.arassec.jptp.core.datatype.UnsignedShort;
import lombok.Getter;

import java.util.Arrays;

public enum ContainerType {

    COMMAND(UnsignedShort.valueOf((short) 0x0001), "Command"),
    DATA(UnsignedShort.valueOf((short) 0x0002), "Data"),
    RESPONSE(UnsignedShort.valueOf((short) 0x0003), "Response"),
    EVENT(UnsignedShort.valueOf((short) 0x0004), "Event");

    @Getter
    private final UnsignedShort type;

    @Getter
    private final String name;

    ContainerType(UnsignedShort type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("ContainerType[0x%04X - %s]", type.value(), name);
    }

    public static ContainerType valueOf(UnsignedShort type) {
        return Arrays.stream(ContainerType.values())
                .filter(v -> v.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ContainerType value: " + type));
    }

}
