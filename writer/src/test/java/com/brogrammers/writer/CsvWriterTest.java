package com.brogrammers.writer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.brogrammers.model.TestModel;
import com.google.common.collect.Lists;

public class CsvWriterTest {

    public Writer writter;

    @Before
    public void setup() {
        writter = mock(Writer.class);
    }


    @Test
    public void should_delegate_writes_to_passed_writer() throws IOException {
        //given
        List<TestModel> content = Lists.newArrayList(
                new TestModel("foo", "bar", 100L, 50),
                new TestModel("baz", "foob", 5000L, 300)
        );

        CsvWriter<TestModel> csvWriter = CsvWriter.create(writter);

        //when
        csvWriter.write(content);

        //then

        //The number of times the append is called is the sum of all the fields written and the line separators and field separators.
        verify(writter, times(15)).append(any(String.class));
        verify(writter, times(1)).append(System.lineSeparator());
        verify(writter, times(2)).flush();
    }

    @Test
    public void should_close_the_given_writer() throws IOException {
        //given
        CsvWriter<TestModel> csvWriter = CsvWriter.create(writter);

        //when
        csvWriter.close();

        //then
        verify(writter, times(1)).close();
    }

    @Test(expected = IllegalStateException.class)
    public void should_throw_IllegalStateException_when_IOException_is_thrown_in_close() throws IOException {
        //given
        CsvWriter<TestModel> csvWriter = CsvWriter.create(writter);
        doThrow(new IOException()).when(writter).close();

        //when
        csvWriter.close();
    }

    @Test(expected = IllegalStateException.class)
    public void should_throw_IllegalStateException_when_IOException_is_thrown_in_append() throws IOException {
        //given
        List<TestModel> content = Lists.newArrayList(
                new TestModel("foo", "bar", 100L, 50),
                new TestModel("baz", "foob", 5000L, 300)
        );
        CsvWriter<TestModel> csvWriter = CsvWriter.create(writter);
        doThrow(new IOException()).when(writter).append(any(String.class));

        //when
        csvWriter.write(content);
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_NullPointerException_when_given_writer_is_null() {
        CsvWriter.create(null);
    }
}
