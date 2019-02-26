package com.github.jsontemplate.main;


import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.github.jsontemplate.test.ParserUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FloatTest {

    @Test
    void test_randomFloatField() {
        DocumentContext document = parse("{aField : @f}");
        assertThat(document.read("$.aField", Float.class), is(notNullValue()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"5", "5.0", "5f", "5.0f", "10.4F", "9E-2F", "3.8E8"})
    void test_fixedFloatField(String value) {
        DocumentContext document = parse(String.format("{aField : @f(%s)}", value));
        assertThat(document.read("$.aField", Float.class), is(Float.parseFloat(value)));
    }

    @Test
    void test_enumeratedFloatField() {
        DocumentContext document = parse("{aField : @f(1.2, 2.34, 4.567, 5.7)}");
        assertThat(document.read("$.aField", Float.class), isIn(new Float[]{1.2f, 2.34f, 4.567f, 5.7f}));
    }

    @RepeatedTest(20)
    void test_minParamFloatField() {
        DocumentContext document = parse("{aField : @f(min=11.22)}");
        assertThat(document.read("$.aField", Float.class), greaterThanOrEqualTo(11.22f));
    }

    @RepeatedTest(20)
    void test_negativeMinParamFloatField() {
        DocumentContext document = parse("{aField : @f(min=-11.22)}");
        assertThat(document.read("$.aField", Float.class), greaterThanOrEqualTo(-11.22f));
    }

    @RepeatedTest(20)
    void test_negativeMaxParamFloatField() {
        DocumentContext document = parse("{aField : @f(max=-11.22)}");
        assertThat(document.read("$.aField", Float.class), lessThanOrEqualTo(-11.22f));
    }

    @RepeatedTest(20)
    void test_minMaxParamFloatFieldPositive() {
        DocumentContext document = parse("{aField : @f(min=11.22, max=22.33)}");
        assertThat(document.read("$.aField", Float.class), allOf(
                greaterThanOrEqualTo(11.22f), lessThanOrEqualTo(22.33f)));
    }

    @RepeatedTest(20)
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

    @Test
    void test_invalidRangeFloatField() {
        assertThrows(IllegalArgumentException.class, () -> parse("{aField : @f(min=20, max=10)}"));
    }

    @Test
    void test_nullFloatField() {
        DocumentContext document = parse("{aField : @f(null)}");
        assertThat(document.read("$.aField", Float.class), is(nullValue()));
    }
}


