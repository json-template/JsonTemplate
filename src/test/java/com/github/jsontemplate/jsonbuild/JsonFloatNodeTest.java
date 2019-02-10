package com.github.jsontemplate.jsonbuild;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class JsonFloatNodeTest {

    @Test
    public void testOf() {
        float value = 1.234f;
        JsonFloatNode node = JsonFloatNode.of(value);
        String printedValue = node.print();

        assertThat(Float.parseFloat(printedValue), is(value));
    }

}