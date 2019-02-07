package com.github.jsontemplate.main;


import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static com.github.jsontemplate.test.TestUtils.*;

@RunWith(JUnit4.class)
public class StringTest {

    private static final int STRING_LENGTH = 5;

    @Test
    public void test_randomStringField() {
        DocumentContext document = parse("{aField : @s}");
        assertThat(document.read("$.aField", String.class).length(), is(STRING_LENGTH));
    }

    @Test
    public void test_fixedStringField() {
        DocumentContext document = parse("{aField : @s(myValue)}");
        assertThat(document.read("$.aField", String.class), is("myValue"));
    }

    @Test
    public void test_enumeratedStringField() {
        DocumentContext document = parse("{aField : @s(A, B, C, D)}");
        assertThat(document.read("$.aField", String.class), isIn(new String[]{"A", "B", "C", "D"}));
    }


    @Test
    public void test_sizedStringField() {
        DocumentContext document = parse("{aField : @s(size=10)}");
        assertThat(document.read("$.aField", String.class).length(), is(10));
    }

    @Test
    public void test_minParamStringField() {
        DocumentContext document = parse("{aField : @s(min=11)}");
        assertThat(document.read("$.aField", String.class).length(), greaterThanOrEqualTo(11));
    }

    @Test
    public void test_minMaxParamStringField() {
        DocumentContext document = parse("{aField : @s(min=10, max=20)}");
        assertThat(document.read("$.aField", String.class).length(), allOf(
                greaterThanOrEqualTo(10), lessThanOrEqualTo(20)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_invalidParamStringField() {
        parse("{aField : @s(length=20)}");
    }

}


