package com.github.jsontemplate.templatetests;

import com.github.jsontemplate.JsonTemplate;
import com.github.jsontemplate.valueproducer.IntegerValueProducer;
import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import static com.github.jsontemplate.templatetests.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class DefaultTypeTest {

    @Test
    void test_DefaultStringTypeAsDefaultType() {
        DocumentContext document = parse(new JsonTemplate("{fieldA, fieldB}"));
        assertThat(document.read("$.fieldA"), is(nullValue()));
        assertThat(document.read("$.fieldB"), is(nullValue()));
    }

    @Test
    void test_simpleDefaultType() {
        DocumentContext document = parse(new JsonTemplate("@i{fieldA, fieldB}"));
        assertThat(document.read("$.fieldA", Integer.class), is(notNullValue()));
        assertThat(document.read("$.fieldB", Integer.class), is(notNullValue()));
    }

    @Test
    void test_overwriteDefaultType() {
        DocumentContext document = parse(new JsonTemplate("@i{fieldA, fieldB : @f { fieldC }}"));
        assertThat(document.read("$.fieldA", Integer.class), is(notNullValue()));
        assertThat(document.read("$.fieldB.fieldC", Float.class), is(notNullValue()));
    }

    @Test
    void test_simpleParamerizedDefaultType() {
        DocumentContext document = parse(new JsonTemplate("@s(length=10){fieldA, fieldB: @s(length=20)}"));
        assertThat(document.read("$.fieldA", String.class).length(), is(10));
        assertThat(document.read("$.fieldB", String.class).length(), is(20));
    }

    @Test
    void test_customSimpleTypeAsDefaultType() {
        DocumentContext document = parse(new JsonTemplate("@ls:@s(length=20), @ls{name, role}"));
        assertThat(document.read("$.name", String.class).length(), is(20));
        assertThat(document.read("$.role", String.class).length(), is(20));
    }

    @Test
    void test_customObjectTypeAsDefaultType() {
        DocumentContext document = parse(new JsonTemplate("@address:@s{city,street,number:@i}, @address{home, office}"));
        assertThat(document.read("$.home.city", String.class), is(notNullValue()));
        assertThat(document.read("$.home.street", String.class), is(notNullValue()));
        assertThat(document.read("$.home.number", Integer.class), is(notNullValue()));
        assertThat(document.read("$.office.city", String.class), is(notNullValue()));
        assertThat(document.read("$.office.street", String.class), is(notNullValue()));
        assertThat(document.read("$.office.number", Integer.class), is(notNullValue()));
    }

    @Test
    void test_customArrayTypeAsDefaultType() {
        DocumentContext document = parse(new JsonTemplate("@names:@s[](3), @names{group1, group2}"));
        assertThat(document.read("$.group1[0]", String.class), is(notNullValue()));
        assertThat(document.read("$.group1[1]", String.class), is(notNullValue()));
        assertThat(document.read("$.group1[2]", String.class), is(notNullValue()));
        assertThat(document.read("$.group2[0]", String.class), is(notNullValue()));
        assertThat(document.read("$.group2[1]", String.class), is(notNullValue()));
        assertThat(document.read("$.group2[2]", String.class), is(notNullValue()));
    }

    @Test
    void test_changeDefaultType() {
        JsonTemplate jsonTemplate = new JsonTemplate("{aField,bField}")
                .withDefaultTypeName(IntegerValueProducer.TYPE_NAME);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.aField", Integer.class), is(notNullValue()));
        assertThat(document.read("$.bField", Integer.class), is(notNullValue()));
    }

    @Test
    void testSmartType() {
        JsonTemplate jsonTemplate =
                new JsonTemplate("{name:John, age:20, height:1.8, male:true, role: null}");
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.name", String.class), is("John"));
        assertThat(document.read("$.age", Integer.class), is(20));
        assertThat(document.read("$.height", Float.class), is(1.8f));
        assertThat(document.read("$.male", Boolean.class), is(true));
        assertThat(document.read("$.role"), is(nullValue()));
    }

    @Test
    void testSmartTypeWithArray() {
        JsonTemplate jsonTemplate =
                new JsonTemplate("[John, 20, 1.8, true, null]");
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$[0]", String.class), is("John"));
        assertThat(document.read("$[1]", Integer.class), is(20));
        assertThat(document.read("$[2]", Float.class), is(1.8f));
        assertThat(document.read("$[3]", Boolean.class), is(true));
        assertThat(document.read("$[4]"), is(nullValue()));
    }
}
