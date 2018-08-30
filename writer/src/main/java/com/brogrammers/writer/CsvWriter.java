package com.brogrammers.writer;

import static com.brogrammers.utilities.Reflections.runGetter;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.reflections.ReflectionUtils;

public class CsvWriter<T> implements AutoCloseable {

    private static final String DEFAULT_FIELD_DELIMITER = ",";
    private boolean newLine = false;
    private Writer writer;

    public static <T> CsvWriter<T> newInstance(Writer writer) {
        return new CsvWriter<>(writer);
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void write(List<T> content) {
        for (int index = 0; index < content.size(); index++) {
            //Only add a new line when there is more than one element in the contents to write
            newLine = index > 0;
            try {
                write(content.get(index));
                writer.flush();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private CsvWriter(Writer writer) {
        this.writer = Objects.requireNonNull(writer);
    }

    private void write(T content) throws IOException {
        Set<Field> fields = ReflectionUtils.getAllFields(content.getClass());

        Iterator<Field> iterator = fields.iterator();

        if (newLine) {
            writer.append(System.lineSeparator());
        }
        boolean isLastField = false;
        while (iterator.hasNext()) {
            Field field = iterator.next();

            //We need to ignore synthetic fields possibly introduced by external tools in order not to output those too
            if (field.isSynthetic()) {
                continue;
            }
            if (!iterator.hasNext()) {
                isLastField = true;
            }

            writer.append(runGetter(field, content).toString());
            if (!isLastField) {
                writer.append(DEFAULT_FIELD_DELIMITER);
            }
        }
    }
}
