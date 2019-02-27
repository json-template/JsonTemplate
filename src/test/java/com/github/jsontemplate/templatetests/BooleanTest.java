package com.github.jsontemplate.templatetests;


import com.github.jsontemplate.JsonTemplate;
import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import static com.github.jsontemplate.templatetests.ParserUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BooleanTest {

    @Test
    void test_randomBooleanField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @b}"));
        assertThat(document.read("$.aField", Boolean.class), anyOf(is(true), is(false)));
    }

    @Test
    void test_trueBooleanField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @b(true)}"));
        assertThat(document.read("$.aField", Boolean.class), is(true));
    }

    @Test
    void test_falseBooleanField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @b(false)}"));
        assertThat(document.read("$.aField", Boolean.class), is(false));
    }

    @Test
    void test_invalidBooleanField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @b(yes)}"));
        assertThat(document.read("$.aField", Boolean.class), is(false));
    }

    @Test
    void test_nullBooleanField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @b(null)}"));
        assertThat(document.read("$.aField", Boolean.class), is(nullValue()));
    }

    @Test
    void test_enumeratedBooleanField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @b(true, true, false)}"));
        assertThat(document.read("$.aField", Boolean.class), isIn(new Boolean[]{true, false}));
    }

    @Test
    void test_paramBooleanField() {
        assertThrows(UnsupportedOperationException.class,
                () -> parse(new JsonTemplate("{aField : @b(min=11)}")));

    }

}


