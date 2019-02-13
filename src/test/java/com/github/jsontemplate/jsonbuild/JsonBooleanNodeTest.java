package com.github.jsontemplate.jsonbuild;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class JsonBooleanNodeTest {

    @Test
    void testOf() {
        boolean value = false;
        JsonBooleanNode node = JsonBooleanNode.of(value);
        String printedValue = node.print();

        assertThat(Boolean.parseBoolean(printedValue), is(value));
    }
}