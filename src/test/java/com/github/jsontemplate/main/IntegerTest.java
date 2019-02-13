package com.github.jsontemplate.main;


import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import static com.github.jsontemplate.test.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntegerTest {

    @Test
    void test_randomIntegerField() {
        DocumentContext document = parse("{aField : @i}");
        assertThat(document.read("$.aField", Integer.class), is(notNullValue()));
    }

    @Test
    void test_fixedIntegerField() {
        DocumentContext document = parse("{aField : @i(5)}");
        assertThat(document.read("$.aField", Integer.class), is(5));
    }

    @Test
    void test_enumeratedIntegerField() {
        DocumentContext document = parse("{aField : @i(20, 30, 40, 50)}");
        assertThat(document.read("$.aField", Integer.class), isIn(new Integer[]{20, 30, 40, 50}));
    }

    @Test
    void test_minParamIntegerField() {
        DocumentContext document = parse("{aField : @i(min=11)}");
        assertThat(document.read("$.aField", Integer.class), greaterThanOrEqualTo(11));
    }

    @Test
    void test_minMaxParamIntegerField() {
        DocumentContext document = parse("{aField : @i(min=10, max=20)}");
        assertThat(document.read("$.aField", Integer.class), allOf(
                greaterThanOrEqualTo(10), lessThanOrEqualTo(20)));
    }

    @Test
    void test_invalidParamIntegerField() {
        assertThrows(IllegalArgumentException.class, () -> {
            DocumentContext document = parse("{aField : @i(length=20)}");
            assertThat(document.read("$.aField", Integer.class), is(5));
        });
    }

}


