package no.ssb.jsontemplate.templatetests;


import com.jayway.jsonpath.DocumentContext;
import no.ssb.jsontemplate.JsonTemplate;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static no.ssb.jsontemplate.templatetests.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntegerTest {

    @Test
    void test_randomIntegerField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @i}"));
        assertThat(document.read("$.aField", Integer.class), is(notNullValue()));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void test_fixedIntegerField(int candidate) {
        DocumentContext document = parse(new JsonTemplate(String.format("{aField : @i(%d)}", candidate)));
        assertThat(document.read("$.aField", Integer.class), is(candidate));
    }

    @Test
    void test_enumeratedIntegerField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @i(20, 30, 40, 50)}"));
        assertThat(document.read("$.aField", Integer.class), isIn(new Integer[]{20, 30, 40, 50}));
    }

    @RepeatedTest(TestUtils.REPEATED_COUNT)
    void test_minParamIntegerField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @i(min=11)}"));
        assertThat(document.read("$.aField", Integer.class), greaterThanOrEqualTo(11));
    }

    @RepeatedTest(TestUtils.REPEATED_COUNT)
    void test_minMaxParamIntegerField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @i(min=10, max=20)}"));
        assertThat(document.read("$.aField", Integer.class), allOf(
                greaterThanOrEqualTo(10), lessThanOrEqualTo(20)));
    }

    @RepeatedTest(TestUtils.REPEATED_COUNT)
    void test_minMaxNegativeParamIntegerField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @i(min=-20, max=-10)}"));
        assertThat(document.read("$.aField", Integer.class), allOf(
                greaterThanOrEqualTo(-20), lessThanOrEqualTo(-10)));
    }

    @Test
    void test_invalidParamIntegerField() {
        JsonTemplate template = new JsonTemplate("{aField : @i(length=20)}");
        assertThrows(IllegalArgumentException.class, () -> parse(template));
    }

    @Test
    void test_invalidRangeIntegerField() {
        JsonTemplate template = new JsonTemplate("{aField : @i(min=20, max=10)}");
        assertThrows(IllegalArgumentException.class, () -> parse(template));
    }

    @Test
    void test_nullIntegerField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @i(null)}"));
        assertThat(document.read("$.aField", Integer.class), is(nullValue()));
    }
}


