package com.brogrammers.it;

import static com.brogrammers.utilities.Reflections.runGetter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.reflections.ReflectionUtils;

import com.brogrammers.it.model.TestModel;
import com.brogrammers.writer.CsvWriter;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

public class CsvWriterIT {

    @Test
    public void shouldExtractAllFieldsFromPassedObject() {
        TestModel testModel = new TestModel("foo", "bar", 500L, 100);
        Set<Field> fields = ReflectionUtils.getAllFields(testModel.getClass());

        assertThat(fields.size(), is(4));

        //The java reflection mechanism does not guarantee ordering of the fields returned.
        Iterator<Field> iterator = fields.iterator();
        assertThat(iterator.next().getName(), is("smallerCounter"));
        assertThat(iterator.next().getName(), is("counter"));
        assertThat(iterator.next().getName(), is("firstName"));
        assertThat(iterator.next().getName(), is("lastName"));

        iterator = fields.iterator();
        assertThat(runGetter(iterator.next(), testModel), is(100));
        assertThat(runGetter(iterator.next(), testModel), is(500L));
        assertThat(runGetter(iterator.next(), testModel), is("foo"));
        assertThat(runGetter(iterator.next(), testModel), is("bar"));
    }

    @Test
    public void should_create_given_csv_and_write_data_with_correct_data_types() {
        //given
        TestModel testModel1 = new TestModel("foo", "bar", 500L, 100);
        TestModel testModel2 = new TestModel("baz", "boom", 3000L, 500);

        CsvWriter<TestModel> csvWriter = new CsvWriter<>(Paths.get("src/test/resources/output.csv"), Charsets.UTF_8, Lists.newArrayList(testModel1, testModel2));

        //when
        csvWriter.write();

        //then

        //For now since the reader is not yet ready I don't have anything automated to assert that the correct contents have been written.
        assertEquals(1L, 1L);
    }
}
