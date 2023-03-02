package no.ssb.jsontemplate.templatetests;

import com.jayway.jsonpath.DocumentContext;
import no.ssb.jsontemplate.JsonTemplate;
import org.junit.jupiter.api.Test;

import static no.ssb.jsontemplate.templatetests.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ArrayTest {

    @Test
    void test_emptyArray() {
        DocumentContext document = parse(new JsonTemplate("{anArray : @s[]}"));
        assertThat(document.read("$.anArray.length()", Integer.class), is(0));
    }

    @Test
    void test_nonEmptyArraySingleParam() {
        DocumentContext document = parse(new JsonTemplate("{anArray : @s[](3)}"));
        assertThat(document.read("$.anArray.length()", Integer.class), is(3));
    }

    @Test
    void test_nonEmptyArrayWithParamSize() {
        DocumentContext document = parse(new JsonTemplate("{anArray : @s[](size=3)}"));
        assertThat(document.read("$.anArray.length()", Integer.class), is(3));
    }

    @Test
    void test_nonEmptyArrayWithMinMaxList() {
        DocumentContext document = parse(new JsonTemplate("{anArray : @s[](1, 10)}"));
        assertThat(document.read("$.anArray.length()", Integer.class),
                allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(10)));
    }

    @Test
    void test_nonEmptyArrayWithMinMaxMap() {
        DocumentContext document = parse(new JsonTemplate("{anArray : @s[](min=1, max=10)}"));
        assertThat(document.read("$.anArray.length()", Integer.class),
                allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(10)));
    }

    @Test
    void test_rootAsArray() {
        DocumentContext document = parse(new JsonTemplate("@s[](min=1, max=10)"));
        assertThat(document.read("$.length()", Integer.class),
                allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(10)));
    }

    @Test
    void test_arrayWithElements() {
        DocumentContext document = parse(new JsonTemplate("{anArray:@s[1, 2, 3, 4]}"));
        assertThat(document.read("$.anArray.length()", Integer.class), is(4));
    }

    @Test
    void test_arrayWithElementsAndSizeParam() {
        DocumentContext document = parse(new JsonTemplate("{anArray:@s[1, 2, 3, 4](6)}"));
        assertThat(document.read("$.anArray.length()", Integer.class), is(6));
        assertThat(document.read("$.anArray[0]", String.class), is("1"));
        assertThat(document.read("$.anArray[1]", String.class), is("2"));
        assertThat(document.read("$.anArray[2]", String.class), is("3"));
        assertThat(document.read("$.anArray[3]", String.class), is("4"));
    }

    @Test
    void test_arrayWithElementsAndSmallSizeParam() {
        DocumentContext document = parse(new JsonTemplate("{anArray:@s[1, 2, 3, 4](2)}"));
        assertThat(document.read("$.anArray.length()", Integer.class), is(4));
        assertThat(document.read("$.anArray[0]", String.class), is("1"));
        assertThat(document.read("$.anArray[1]", String.class), is("2"));
        assertThat(document.read("$.anArray[2]", String.class), is("3"));
        assertThat(document.read("$.anArray[3]", String.class), is("4"));
    }

    @Test
    void test_arrayWithoutDefaultType() {
        DocumentContext document = parse(new JsonTemplate("{anArray:[@s(1), @s(2), @s(3), @s(4)]}"));
        assertThat(document.read("$.anArray.length()", Integer.class), is(4));
        assertThat(document.read("$.anArray[0]", String.class), is("1"));
        assertThat(document.read("$.anArray[1]", String.class), is("2"));
        assertThat(document.read("$.anArray[2]", String.class), is("3"));
        assertThat(document.read("$.anArray[3]", String.class), is("4"));
    }

    @Test
    void test_arrayMixedType() {
        DocumentContext document = parse(new JsonTemplate("{anArray:@s[1, @i(2), @b(false), @s(4)]}"));
        assertThat(document.read("$.anArray.length()", Integer.class), is(4));
        assertThat(document.read("$.anArray[0]", String.class), is("1"));
        assertThat(document.read("$.anArray[1]", Integer.class), is(2));
        assertThat(document.read("$.anArray[2]", Boolean.class), is(false));
        assertThat(document.read("$.anArray[3]", String.class), is("4"));
    }

}
