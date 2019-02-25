package com.github.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import static com.github.jsontemplate.test.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


class TypeTest {

    @Test
    void test_typeWithoutParam() {
        DocumentContext document = parse(
                "@address:{city:@s,street:@s,number:@i},{office:@address, home:@address}");
        assertThat(document.read("$.office.city", String.class), is(notNullValue()));
        assertThat(document.read("$.office.street", String.class), is(notNullValue()));
        assertThat(document.read("$.office.number", Integer.class), is(notNullValue()));
        assertThat(document.read("$.home.city", String.class), is(notNullValue()));
        assertThat(document.read("$.home.street", String.class), is(notNullValue()));
        assertThat(document.read("$.home.number", Integer.class), is(notNullValue()));

    }

    @Test
    void test_typeWithSingleParam() {
        DocumentContext document = parse(
                "@address:{city:@s(Amsterdam),street:@s,number:@i(5)}," +
                "{office:@address, home:@address}");
        assertThat(document.read("$.office.city", String.class), is("Amsterdam"));
        assertThat(document.read("$.office.street", String.class), is(notNullValue()));
        assertThat(document.read("$.office.number", Integer.class), is(5));
        assertThat(document.read("$.home.city", String.class), is("Amsterdam"));
        assertThat(document.read("$.home.street", String.class), is(notNullValue()));
        assertThat(document.read("$.home.number", Integer.class), is(5));
    }

    @Test
    void test_typeWithListParam() {
        DocumentContext document = parse(
                "@address:{city:@s(Amsterdam, Utrecht), street:@s, number:@i(5, 10, 15)}," +
                "{office:@address, home:@address }");
        assertThat(document.read("$.office.city", String.class), isIn(new String[]{"Amsterdam", "Utrecht"}));
        assertThat(document.read("$.office.street", String.class), is(notNullValue()));
        assertThat(document.read("$.office.number", Integer.class), isIn(new Integer[]{5, 10, 15}));
        assertThat(document.read("$.home.city", String.class), isIn(new String[]{"Amsterdam", "Utrecht"}));
        assertThat(document.read("$.home.street", String.class), is(notNullValue()));
        assertThat(document.read("$.home.number", Integer.class), isIn(new Integer[]{5, 10, 15}));
    }

    @Test
    void test_typeWithMapParam() {
        DocumentContext document = parse(
                "@address:{city:@s(length=10),street:@s(length=20),number:@i(min=1000)}," +
                "{office:@address, home:@address}");
        assertThat(document.read("$.office.city", String.class).length(), is(10));
        assertThat(document.read("$.office.street", String.class).length(), is(20));
        assertThat(document.read("$.office.number", Integer.class), greaterThanOrEqualTo(1000));
        assertThat(document.read("$.home.city", String.class).length(), is(10));
        assertThat(document.read("$.home.street", String.class).length(), is(20));
        assertThat(document.read("$.home.number", Integer.class), greaterThanOrEqualTo(1000));
    }

    @Test
    void test_nestedObjectType() {
        DocumentContext document = parse(
                "@address : {city, street, number:@i}," +
                        "@person : @address{office, home}," +
                        "@person[](2)");
        assertThat(document.read("$[0].office.city", String.class), is(notNullValue()));
        assertThat(document.read("$[0].office.street", String.class), is(notNullValue()));
        assertThat(document.read("$[0].office.number", Integer.class), is(notNullValue()));
        assertThat(document.read("$[0].home.city", String.class), is(notNullValue()));
        assertThat(document.read("$[0].home.street", String.class), is(notNullValue()));
        assertThat(document.read("$[0].home.number", Integer.class), is(notNullValue()));
        assertThat(document.read("$[1].office.city", String.class), is(notNullValue()));
        assertThat(document.read("$[1].office.street", String.class), is(notNullValue()));
        assertThat(document.read("$[1].office.number", Integer.class), is(notNullValue()));
        assertThat(document.read("$[1].home.city", String.class), is(notNullValue()));
        assertThat(document.read("$[1].home.street", String.class), is(notNullValue()));
        assertThat(document.read("$[1].home.number", Integer.class), is(notNullValue()));
    }

    @Test
    void test_nestedArrayType() {
        DocumentContext document = parse(
                "@methods : [](3)," +
                        "@classes : @methods[](2)," +
                        "@classes[](2)");
        assertThat(document.read("$.length()", Integer.class), is(2));
        assertThat(document.read("$[0].length()", Integer.class), is(2));
        assertThat(document.read("$[0].[0].length()", Integer.class), is(3));
        assertThat(document.read("$[0].[1].length()", Integer.class), is(3));
        assertThat(document.read("$[1].length()", Integer.class), is(2));
        assertThat(document.read("$[1].[0].length()", Integer.class), is(3));
        assertThat(document.read("$[1].[1].length()", Integer.class), is(3));
    }

    @Test
    void test_objectInArrayType() {
        DocumentContext document = parse(
                "@methods : {name, paramCount:@i(min=0, max=5)}," +
                        "@classes : @methods[](2)," +
                        "@classes[](2)");
        assertThat(document.read("$.length()", Integer.class), is(2));
        assertThat(document.read("$[0].length()", Integer.class), is(2));
        assertThat(document.read("$[0].[0].name", String.class), is(notNullValue()));
        assertThat(document.read("$[0].[0].paramCount", Integer.class), is(
                both(greaterThanOrEqualTo(0)).and(lessThanOrEqualTo(5))));
        assertThat(document.read("$[1].length()", Integer.class), is(2));
        assertThat(document.read("$[1].[0].name", String.class), is(notNullValue()));
        assertThat(document.read("$[1].[0].paramCount", Integer.class), is(
                both(greaterThanOrEqualTo(0)).and(lessThanOrEqualTo(5))));
    }
}
