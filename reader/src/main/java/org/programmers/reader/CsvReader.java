package org.programmers.reader;

import java.util.List;

public class CsvReader<T> {

    private final BasicReader reader;

    public CsvReader() {
        //TODO here in the future make choose strategy as arg: SINGLE, THREADED, file lock, etc.
        reader = new BasicReader();
    }

    public List<T> read(String file, Class<T> clazz) {
        throw new UnsupportedOperationException();
    }
}
