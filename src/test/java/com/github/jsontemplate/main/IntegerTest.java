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
import static org.hamcrest.Matchers.notNullValue;
import static com.github.jsontemplate.test.TestUtils.*;

@RunWith(JUnit4.class)
public class IntegerTest {

    @Test
    public void test_randomIntegerField() {
        DocumentContext document = parse("{aField : @i}");
        assertThat(document.read("$.aField", Integer.class), is(notNullValue()));
    }

    @Test
    public void test_fixedIntegerField() {
        DocumentContext document = parse("{aField : @i(5)}");
        assertThat(document.read("$.aField", Integer.class), is(5));
    }

    @Test
    public void test_enumeratedIntegerField() {
        DocumentContext document = parse("{aField : @i(20, 30, 40, 50)}");
        assertThat(document.read("$.aField", Integer.class), isIn(new Integer[]{20, 30, 40, 50}));
    }

    @Test
    public void test_minParamIntegerField() {
        DocumentContext document = parse("{aField : @i(min=11)}");
        assertThat(document.read("$.aField", Integer.class), greaterThanOrEqualTo(11));
    }

    @Test
    public void test_minMaxParamIntegerField() {
        DocumentContext document = parse("{aField : @i(min=10, max=20)}");
        assertThat(document.read("$.aField", Integer.class), allOf(
                greaterThanOrEqualTo(10), lessThanOrEqualTo(20)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_invalidParamIntegerField() {
        DocumentContext document = parse("{aField : @i(length=20)}");
        assertThat(document.read("$.aField", Integer.class), is(5));
    }

}


