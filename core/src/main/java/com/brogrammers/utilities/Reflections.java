package com.brogrammers.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.reflections.ReflectionUtils;

public final class Reflections {
    private Reflections() {

    }

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
