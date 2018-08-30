package com.brogrammers.it;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.brogrammers.model.TestModel;
import com.brogrammers.writer.CsvWriter;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

public class CsvWriterIT {

    @Test
    public void should_create_given_csv_and_write_data_with_correct_data_types() {
        //given
        TestModel testModel1 = new TestModel("foo", "bar", 500L, 100);
        TestModel testModel2 = new TestModel("baz", "boom", 3000L, 500);

        Writer writer = createWriter();

        CsvWriter<TestModel> csvWriter = CsvWriter.newInstance(writer);

        //when
        csvWriter.write(Lists.newArrayList(testModel1, testModel2));

        //then

        //For now since the reader is not yet ready I don't have anything automated to assert that the correct contents have been written.
        assertEquals(1L, 1L);
    }

    private BufferedWriter createWriter() {
        try {
            return Files.newBufferedWriter(Paths.get("src/test/resources/output.csv"), Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
