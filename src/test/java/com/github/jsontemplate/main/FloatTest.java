package com.github.jsontemplate.main;


import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import static com.github.jsontemplate.test.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FloatTest {

    @Test
    void test_randomFloatField() {
        DocumentContext document = parse("{aField : @f}");
        assertThat(document.read("$.aField", Float.class), is(notNullValue()));
    }

    @Test
    void test_fixedFloatField() {
        DocumentContext document = parse("{aField : @f(5)}");
        assertThat(document.read("$.aField", Float.class), is(5f));
    }

    @Test
    void test_enumeratedFloatField() {
        DocumentContext document = parse("{aField : @f(1.2, 2.34, 4.567, 5.7)}");
        assertThat(document.read("$.aField", Float.class), isIn(new Float[]{1.2f, 2.34f, 4.567f, 5.7f}));
    }

    @Test
    void test_minParamFloatField() {
        DocumentContext document = parse("{aField : @f(min=11.22)}");
        assertThat(document.read("$.aField", Float.class), greaterThanOrEqualTo(11.22f));
    }

    @Test
    void test_minMaxParamFloatFieldPositive() {
        DocumentContext document = parse("{aField : @f(min=11.22, max=22.33)}");
        assertThat(document.read("$.aField", Float.class), allOf(
                greaterThanOrEqualTo(11.22f), lessThanOrEqualTo(22.33f)));
    }

    @Test
    void test_minMaxParamFloatFieldNegative() {
        DocumentContext document = parse("{aField : @f(min=-22.11, max=-11.33)}");
        assertThat(document.read("$.aField", Float.class), allOf(
                greaterThanOrEqualTo(-22.11f), lessThanOrEqualTo(-11.33f)));
    }

    @Test
    void test_invalidParamFloatField() {
        assertThrows(IllegalArgumentException.class, () ->
                parse("{aField : @f(length=20)}"));
    }

}


