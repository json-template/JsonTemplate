package no.ssb.jsontemplate.templatetests;


import no.ssb.jsontemplate.JsonTemplate;
import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BooleanTest {

    @Test
    void test_randomBooleanField() {
        DocumentContext document = TestUtils.parse(new JsonTemplate("{aField : @b}"));
        assertThat(document.read("$.aField", Boolean.class), anyOf(is(true), is(false)));
    }

    @Test
    void test_trueBooleanField() {
        DocumentContext document = TestUtils.parse(new JsonTemplate("{aField : @b(true)}"));
        assertThat(document.read("$.aField", Boolean.class), is(true));
    }

    @Test
    void test_falseBooleanField() {
        DocumentContext document = TestUtils.parse(new JsonTemplate("{aField : @b(false)}"));
        assertThat(document.read("$.aField", Boolean.class), is(false));
    }

    @Test
    void test_invalidBooleanField() {
        DocumentContext document = TestUtils.parse(new JsonTemplate("{aField : @b(yes)}"));
        assertThat(document.read("$.aField", Boolean.class), is(false));
    }

    @Test
    void test_nullBooleanField() {
        DocumentContext document = TestUtils.parse(new JsonTemplate("{aField : @b(null)}"));
        assertThat(document.read("$.aField", Boolean.class), is(nullValue()));
    }

    @Test
    void test_enumeratedBooleanField() {
        DocumentContext document = TestUtils.parse(new JsonTemplate("{aField : @b(true, true, false)}"));
        assertThat(document.read("$.aField", Boolean.class), isIn(new Boolean[]{true, false}));
    }

    @Test
    void test_paramBooleanField() {
        assertThrows(UnsupportedOperationException.class,
                () -> TestUtils.parse(new JsonTemplate("{aField : @b(min=11)}")));

    }

}


