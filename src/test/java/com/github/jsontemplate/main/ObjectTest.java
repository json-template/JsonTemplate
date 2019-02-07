package com.github.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static com.github.jsontemplate.test.TestUtils.*;

@RunWith(JUnit4.class)
public class ObjectTest {

    @Test
    public void test_twoLevelObject() {
        DocumentContext document = parse("{anObject:{aField:@s}}");
        assertThat(document.read("$.anObject.aField", String.class), is(notNullValue()));
    }

    @Test
    public void test_anObjectWithTwoFields() {
        DocumentContext document = parse("{anObject:{aField:@s, bField:@s}}");
        assertThat(document.read("$.anObject.aField", String.class), is(notNullValue()));
        assertThat(document.read("$.anObject.bField", String.class), is(notNullValue()));
    }

    @Test
    public void test_multipleLevelObject() {
        DocumentContext document = parse("{objA:{objB:{objC:{objD:{aField:@s}}}}}");
        assertThat(document.read("$.objA.objB.objC.objD.aField", String.class), is(notNullValue()));
    }

    @Test
    public void test_twoObjects() {
        DocumentContext document = parse("{objA:{fieldA:@s},objB:{fieldB:@i}}");
        assertThat(document.read("$.objA.fieldA", String.class), is(notNullValue()));
        assertThat(document.read("$.objB.fieldB", String.class), is(notNullValue()));
    }

}
