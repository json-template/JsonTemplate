package com.github.jsontemplate.main;


import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static com.github.jsontemplate.test.TestUtils.*;

@RunWith(JUnit4.class)
public class BooleanTest {

    @Test
    public void test_randomBooleanField() {
        DocumentContext document = parse("{aField : @b}");
        assertThat(document.read("$.aField", Boolean.class), anyOf(is(true), is(false)));
    }

    @Test
    public void test_fixedBooleanField() {
        DocumentContext document = parse("{aField : @b(true)}");
        assertThat(document.read("$.aField", Boolean.class), is(true));
    }

    @Test
    public void test_enumeratedBooleanField() {
        DocumentContext document = parse("{aField : @b(true, true, false)}");
        assertThat(document.read("$.aField", Boolean.class), isIn(new Boolean[]{true, false}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test_paramBooleanField() {
        parse("{aField : @b(min=11)}");
    }

}


