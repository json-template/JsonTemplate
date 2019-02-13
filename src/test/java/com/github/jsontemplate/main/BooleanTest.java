package com.github.jsontemplate.main;


import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import static com.github.jsontemplate.test.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BooleanTest {

    @Test
    void test_randomBooleanField() {
        DocumentContext document = parse("{aField : @b}");
        assertThat(document.read("$.aField", Boolean.class), anyOf(is(true), is(false)));
    }

    @Test
    void test_fixedBooleanField() {
        DocumentContext document = parse("{aField : @b(true)}");
        assertThat(document.read("$.aField", Boolean.class), is(true));
    }

    @Test
    void test_enumeratedBooleanField() {
        DocumentContext document = parse("{aField : @b(true, true, false)}");
        assertThat(document.read("$.aField", Boolean.class), isIn(new Boolean[]{true, false}));
    }

    @Test
    void test_paramBooleanField() {
        assertThrows(UnsupportedOperationException.class,
                () -> parse("{aField : @b(min=11)}"));

    }

}


