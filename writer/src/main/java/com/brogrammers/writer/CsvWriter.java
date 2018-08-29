package com.brogrammers.writer;

import static com.brogrammers.utilities.Reflections.runGetter;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.reflections.ReflectionUtils;

public class CsvWriter<T> implements Closeable {

    private static final String DEFAULT_FIELD_DELIMITER = ",";
    private boolean newLine = false;
    private BufferedWriter writer;
    private final Path path;
    private final Charset charset;
    private final List<T> content;

    public CsvWriter(Path path, Charset charset, List<T> content) {
        this.path = Objects.requireNonNull(path);
        this.charset = Objects.requireNonNull(charset);
        this.content = Objects.requireNonNull(content);

        try {
            this.writer = Files.newBufferedWriter(path, charset, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
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

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public void write() {
        for (int index = 0; index < content.size(); index++) {
            newLine = index > 0;
            try {
                write(content.get(index));
                writer.flush();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private void write(T content) throws IOException {
        Set<Field> fields = ReflectionUtils.getAllFields(content.getClass());

        Iterator<Field> iterator = fields.iterator();

        if (newLine) {
            writer.newLine();
        }
        boolean isLastField = false;
        while (iterator.hasNext()) {
            Field field = iterator.next();
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
