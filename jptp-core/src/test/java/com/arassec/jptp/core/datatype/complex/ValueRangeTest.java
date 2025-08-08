package com.arassec.jptp.core.datatype.complex;

import com.arassec.jptp.core.datatype.UnsignedShort;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Base class with utility methods for value range tests.
 */
abstract class ValueRangeTest {

    /**
     * Tests a PTP record with defined value range.
     */
    void testValueRange(Class<?> clazz, CodeExtractor valueExtractor) {
        Method method = findValueOfMethod(clazz);

        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isPublic(field.getModifiers())
                    && Modifier.isStatic(field.getModifiers())) {
                try {
                    Object constantValue = field.get(null);
                    Object result = method.invoke(null, valueExtractor.getCode(constantValue));
                    assertThat(result).isEqualTo(constantValue);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    /**
     * Determines the testees "valueOf" method.
     *
     * @param clazz Class type of the testee.
     * @return The method.
     */
    private Method findValueOfMethod(Class<?> clazz) {
        try {
            Method method = clazz.getMethod("valueOf", UnsignedShort.class);

            if (!Modifier.isPublic(method.getModifiers())
                    || !Modifier.isStatic(method.getModifiers())) {
                throw new IllegalArgumentException("Method is not public and static!");
            }

            return method;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("valueOf() method doesn't exist!");
        }
    }

    /**
     * Defines an extractor for the testee's code, which is supplied to the "valueOf" method as parameter.
     */
    public interface CodeExtractor {

        /**
         * Gets the code to use as parameter in the "valueOf" method.
         *
         * @param instance The record instance.
         * @return The code to use.
         */
        Object getCode(Object instance);
    }

}
