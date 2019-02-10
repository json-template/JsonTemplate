package com.github.jsontemplate.jsonbuild;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class JsonBooleanNodeTest {

    @Test
    public void testOf() {
        boolean value = false;
        JsonBooleanNode node = JsonBooleanNode.of(value);
        String printedValue = node.print();

        assertThat(Boolean.parseBoolean(printedValue), is(value));
    }
}