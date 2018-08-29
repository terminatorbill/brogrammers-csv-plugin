package com.brogrammers.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.reflections.ReflectionUtils;

public final class Reflections {
    private Reflections() {

    }

    /**
     * Searches and invokes through reflection the correct getter method
     * for the provided {@link Field} object.
     * @param field The field to search for the appropriate public getter
     * @param content The object on which we use reflection
     * @param <T>
     * @return The type of the public getter of the field
     * @throws IllegalAccessError if either no appropriate public getter is found or no method at all.
     */
    public static <T> Object runGetter(Field field, T content) {
        for (Method method : ReflectionUtils.getAllMethods(content.getClass())) {
            if (isGet(field, method) || isIs(field, method)) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    try {
                        return method.invoke(content);
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        throw new IllegalAccessError();
    }

    private static boolean isGet(Field field, Method method) {
        return method.getName().startsWith("get") && method.getName().length() == (field.getName().length() + 3);
    }

    private static boolean isIs(Field field, Method method) {
        return method.getName().startsWith("is") && method.getName().length() == (field.getName().length() + 2);
    }
}
