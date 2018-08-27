package com.brogrammers.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
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
        TestModel testModel = new TestModel("foo", "bar", 500L, 100);
        CsvWriter<TestModel> csvWriter = new CsvWriter<>(Paths.get("src/test/resources/output.csv"), Charsets.UTF_8, Lists.newArrayList(testModel));

        //when
        csvWriter.write();

        //then
    }


    private static Object runGetter(Field field, TestModel testModel) {
        for (Method method : ReflectionUtils.getAllMethods(testModel.getClass())) {
            if (isGet(field, method) || isIs(field, method)) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    try {
                        return method.invoke(testModel);
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private static boolean isGet(Field field, Method method) {
        return method.getName().startsWith("get") && method.getName().length() == (field.getName().length() + 3);
    }

    private static boolean isIs(Field field, Method method) {
        return method.getName().startsWith("is") && method.getName().length() == (field.getName().length() + 2);
    }
}
