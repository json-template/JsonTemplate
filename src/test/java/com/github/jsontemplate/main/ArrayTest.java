package com.github.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static com.github.jsontemplate.test.TestUtils.*;

@RunWith(JUnit4.class)
public class ArrayTest {

    @Test
    public void test_emptyArray() {
        DocumentContext document = parse("{anArray : @s[]}");
        assertThat(document.read("$.anArray.length()", Integer.class), is(0));
    }

    @Test
    public void test_nonEmptyArraySingleParam() {
        DocumentContext document = parse("{anArray : @s[](3)}");
        assertThat(document.read("$.anArray.length()", Integer.class), is(3));
    }

    @Test
    public void test_nonEmptyArrayWithParamSize() {
        DocumentContext document = parse("{anArray : @s[](size=3)}");
        assertThat(document.read("$.anArray.length()", Integer.class), is(3));
    }

    @Test
    public void test_nonEmptyArrayWithMinMaxList() {
        DocumentContext document = parse("{anArray : @s[](1, 10)}");
        assertThat(document.read("$.anArray.length()", Integer.class),
                allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(10)));
    }

    @Test
    public void test_nonEmptyArrayWithMinMaxMap() {
        DocumentContext document = parse("{anArray : @s[](min=1, max=10)}");
        assertThat(document.read("$.anArray.length()", Integer.class),
                allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(10)));
    }

    @Test
    public void test_rootAsArray() {
        DocumentContext document = parse("@s[](min=1, max=10)");
        assertThat(document.read("$.length()", Integer.class),
                allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(10)));
    }

    @Test
    public void test_arrayWithElements() {
        DocumentContext document = parse("{anArray:@s[1, 2, 3, 4]}");
        assertThat(document.read("$.anArray.length()", Integer.class), is(4));
    }

    @Test
    public void test_arrayWithElementsAndSizeParam() {
        DocumentContext document = parse("{anArray:@s[1, 2, 3, 4](6)}");
        assertThat(document.read("$.anArray.length()", Integer.class), is(6));
        assertThat(document.read("$.anArray[0]", String.class), is("1"));
        assertThat(document.read("$.anArray[1]", String.class), is("2"));
        assertThat(document.read("$.anArray[2]", String.class), is("3"));
        assertThat(document.read("$.anArray[3]", String.class), is("4"));
    }

    @Test
    public void test_arrayWithElementsAndSmallSizeParam() {
        DocumentContext document = parse("{anArray:@s[1, 2, 3, 4](2)}");
        assertThat(document.read("$.anArray.length()", Integer.class), is(4));
        assertThat(document.read("$.anArray[0]", String.class), is("1"));
        assertThat(document.read("$.anArray[1]", String.class), is("2"));
        assertThat(document.read("$.anArray[2]", String.class), is("3"));
        assertThat(document.read("$.anArray[3]", String.class), is("4"));
    }

    @Test
    public void test_arrayWithoutDefaultType() {
        DocumentContext document = parse("{anArray:[@s(1), @s(2), @s(3), @s(4)]}");
        assertThat(document.read("$.anArray.length()", Integer.class), is(4));
        assertThat(document.read("$.anArray[0]", String.class), is("1"));
        assertThat(document.read("$.anArray[1]", String.class), is("2"));
        assertThat(document.read("$.anArray[2]", String.class), is("3"));
        assertThat(document.read("$.anArray[3]", String.class), is("4"));
    }

    @Test
    public void test_arrayMixedType() {
        DocumentContext document = parse("{anArray:@s[1, @i(2), @b(false), @s(4)]}");
        assertThat(document.read("$.anArray.length()", Integer.class), is(4));
        assertThat(document.read("$.anArray[0]", String.class), is("1"));
        assertThat(document.read("$.anArray[1]", Integer.class), is(2));
        assertThat(document.read("$.anArray[2]", Boolean.class), is(false));
        assertThat(document.read("$.anArray[3]", String.class), is("4"));
    }

}
