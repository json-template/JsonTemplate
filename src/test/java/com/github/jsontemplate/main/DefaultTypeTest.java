package com.github.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import static com.github.jsontemplate.test.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class DefaultTypeTest {

    @Test
    void test_DefaultStringTypeAsDefaultType() {
        DocumentContext document = parse("{fieldA, fieldB}");
        assertThat(document.read("$.fieldA", String.class), is(notNullValue()));
        assertThat(document.read("$.fieldB", String.class), is(notNullValue()));
    }

    @Test
    void test_simpleDefaultType() {
        DocumentContext document = parse("@i{fieldA, fieldB}");
        assertThat(document.read("$.fieldA", Integer.class), is(notNullValue()));
        assertThat(document.read("$.fieldB", Integer.class), is(notNullValue()));
    }

    @Test
    void test_overwriteDefaultType() {
        DocumentContext document = parse("@i{fieldA, fieldB : @f { fieldC }}");
        assertThat(document.read("$.fieldA", Integer.class), is(notNullValue()));
        assertThat(document.read("$.fieldB.fieldC", Float.class), is(notNullValue()));
    }

    @Test
    void test_simpleParamerizedDefaultType() {
        DocumentContext document = parse("@s(length=10){fieldA, fieldB: @s(length=20)}");
        assertThat(document.read("$.fieldA", String.class).length(), is(10));
        assertThat(document.read("$.fieldB", String.class).length(), is(20));
    }

    @Test
    void test_customTypeAsDefaultType() {
        DocumentContext document = parse("@address{home, office, @address:{city,street,number:@i}}");
        assertThat(document.read("$.home.city", String.class), is(notNullValue()));
        assertThat(document.read("$.home.street", String.class), is(notNullValue()));
        assertThat(document.read("$.home.number", Integer.class), is(notNullValue()));
        assertThat(document.read("$.office.city", String.class), is(notNullValue()));
        assertThat(document.read("$.office.street", String.class), is(notNullValue()));
        assertThat(document.read("$.office.number", Integer.class), is(notNullValue()));
    }

    @Test
    void test_customTypeAsDefaultTypeInArray() {
        DocumentContext document = parse("@address[ {@address:{city,street,number:@i}} ](3)");
    }
}
