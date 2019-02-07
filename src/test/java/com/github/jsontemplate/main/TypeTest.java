package com.github.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.notNullValue;
import static com.github.jsontemplate.test.TestUtils.*;


@RunWith(JUnit4.class)
public class TypeTest {

    @Test
    public void test_typeWithoutParam() {
        DocumentContext document = parse("{" +
                "@address:{city:@s,street:@s,number:@i}," +
                "office:@address, home:@address" +
                "}");
        assertThat(document.read("$.office.city", String.class), is(notNullValue()));
        assertThat(document.read("$.office.street", String.class), is(notNullValue()));
        assertThat(document.read("$.office.number", Integer.class), is(notNullValue()));
        assertThat(document.read("$.home.city", String.class), is(notNullValue()));
        assertThat(document.read("$.home.street", String.class), is(notNullValue()));
        assertThat(document.read("$.home.number", Integer.class), is(notNullValue()));

    }

    @Test
    public void test_typeWithSingleParam() {
        DocumentContext document = parse("{" +
                "@address:{city:@s(Amsterdam),street:@s,number:@i(5)}," +
                "office:@address, home:@address" +
                "}");
        assertThat(document.read("$.office.city", String.class), is("Amsterdam"));
        assertThat(document.read("$.office.street", String.class), is(notNullValue()));
        assertThat(document.read("$.office.number", Integer.class), is(5));
        assertThat(document.read("$.home.city", String.class), is("Amsterdam"));
        assertThat(document.read("$.home.street", String.class), is(notNullValue()));
        assertThat(document.read("$.home.number", Integer.class), is(5));
    }

    @Test
    public void test_typeWithListParam() {
        DocumentContext document = parse("{" +
                "@address:{city:@s(Amsterdam, Utrecht),street:@s,number:@i(5, 10, 15)}," +
                "office:@address, home:@address" +
                "}");
        assertThat(document.read("$.office.city", String.class), isIn(new String[]{"Amsterdam", "Utrecht"}));
        assertThat(document.read("$.office.street", String.class), is(notNullValue()));
        assertThat(document.read("$.office.number", Integer.class), isIn(new Integer[]{5, 10, 15}));
        assertThat(document.read("$.home.city", String.class), isIn(new String[]{"Amsterdam", "Utrecht"}));
        assertThat(document.read("$.home.street", String.class), is(notNullValue()));
        assertThat(document.read("$.home.number", Integer.class), isIn(new Integer[]{5, 10, 15}));
    }

    @Test
    public void test_typeWithMapParam() {
        DocumentContext document = parse("{" +
                "@address:{city:@s(size=10),street:@s(size=20),number:@i(min=1000)}," +
                "office:@address, home:@address" +
                "}");
        assertThat(document.read("$.office.city", String.class).length(), is(10));
        assertThat(document.read("$.office.street", String.class).length(), is(20));
        assertThat(document.read("$.office.number", Integer.class), greaterThanOrEqualTo(1000));
        assertThat(document.read("$.home.city", String.class).length(), is(10));
        assertThat(document.read("$.home.street", String.class).length(), is(20));
        assertThat(document.read("$.home.number", Integer.class), greaterThanOrEqualTo(1000));
    }

}
