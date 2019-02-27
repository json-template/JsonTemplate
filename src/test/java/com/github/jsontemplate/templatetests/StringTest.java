package com.github.jsontemplate.templatetests;


import com.github.jsontemplate.JsonTemplate;
import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.github.jsontemplate.templatetests.ParserUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


class StringTest {

    private static final int STRING_LENGTH = 5;

    @Test
    @DisplayName("a random string field")
    void parseRandomString() {
        DocumentContext document = parse(new JsonTemplate("{aField : @s}"));
        assertThat(document.read("$.aField", String.class), is(notNullValue()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"myValue", "123", "1.2.3.4", "100%", "#123", "1*2/3-4"})
    void parseFixedString(String fixedValue) {
        String template = String.format("{aField : @s(%s)}", fixedValue);
        DocumentContext document = parse(new JsonTemplate(template));
        assertThat(document.read("$.aField", String.class), is(fixedValue));
    }

    @Test
    void test_nullString() {
        DocumentContext document = parse(new JsonTemplate("{aField : @s(null)}"));
        assertThat(document.read("$.aField", String.class), is(nullValue()));
    }

    @RepeatedTest(20)
    void test_enumeratedStringField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @s(A, B, C, D)}"));
        assertThat(document.read("$.aField", String.class), isIn(new String[]{"A", "B", "C", "D"}));
    }

    @RepeatedTest(20)
    void test_enumeratedStringFieldWithLiteralNull() {
        DocumentContext document = parse(new JsonTemplate("{aField : @s(A, B, C, null)}"));
        assertThat(document.read("$.aField", String.class), isIn(new String[]{"A", "B", "C", "null"}));
    }

    @Test
    void test_sizedStringField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @s(length=10)}"));
        assertThat(document.read("$.aField", String.class).length(), is(10));
    }

    @Test
    void test_minusSizedStringField() {
        assertThrows(IllegalArgumentException.class,
                () -> parse(new JsonTemplate("{aField : @s(length=-10)}")));
    }

    @RepeatedTest(20)
    void test_minParamStringField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @s(min=11)}"));
        assertThat(document.read("$.aField", String.class).length(), greaterThanOrEqualTo(11));
    }

    @Test
    void test_minusMinParamStringField() {
        assertThrows(IllegalArgumentException.class,
                () -> parse(new JsonTemplate("{aField : @s(min=-1)}")));
    }

    @RepeatedTest(20)
    void test_minMaxParamStringField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @s(min=10, max=20)}"));
        assertThat(document.read("$.aField", String.class).length(), allOf(
                greaterThanOrEqualTo(10), lessThanOrEqualTo(20)));
    }

    @Test
    void test_invalidRangeParamStringField() {
        assertThrows(IllegalArgumentException.class,
                () -> parse(new JsonTemplate("{aField : @s(min=20, max=10)}")));
    }

    @Test
    void test_invalidParamStringField() {
        assertThrows(IllegalArgumentException.class, () ->
                parse(new JsonTemplate("{aField : @s(size=20)}"))
        );
    }

    @Test
    void test_rawString() {
        String rawContent = "!@#$%^&*()-= \t\n{}[]abc";
        DocumentContext document = parse(new JsonTemplate("{aField : @s(`" + rawContent + "`)}"));
        assertThat(document.read("$.aField", String.class), is(rawContent));
    }

}


