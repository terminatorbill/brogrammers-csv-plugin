package com.brogrammers.writer;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.reflections.ReflectionUtils;

public class CsvWriter<T> implements Closeable, Flushable {

    private static final String DEFAULT_FIELD_DELIMITER = ",";
    private static final String DEFAULT_LINE_SEPARATOR =  System.lineSeparator();
    private boolean newLine = false;
    private final Path path;
    private final Charset charset;
    private final List<T> content;
    private final OutputStreamWriter writer;

    public CsvWriter(Path path, Charset charset, List<T> content) {
        this.path = Objects.requireNonNull(path);
        this.charset = Objects.requireNonNull(charset);
        this.content = Objects.requireNonNull(content);

        try {
            this.writer = new OutputStreamWriter(
                    Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING),
                    charset
            );
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void write() {
        for (int index = 0; index < content.size(); index++) {
            newLine = (index != content.size() - 1 || index > 0);
            try {
                write(content.get(index));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private void write(T content) throws IOException {
        Set<Field> fields = ReflectionUtils.getAllFields(content.getClass());

        Iterator<Field> iterator = fields.iterator();

        if (newLine) {
            writer.write(DEFAULT_LINE_SEPARATOR);
        }
        boolean isLastField = false;
        while (iterator.hasNext()) {
            Field field = iterator.next();
            if (!iterator.hasNext()) {
                isLastField = true;
            }

            writer.write(runGetter(field, content).toString());
            if (!isLastField) {
                writer.write(DEFAULT_FIELD_DELIMITER);
            }
        }
    }

    private static <T> Object runGetter(Field field, T content) {
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
