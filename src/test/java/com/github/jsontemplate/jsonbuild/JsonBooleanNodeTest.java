package com.github.jsontemplate.jsonbuild;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class JsonBooleanNodeTest {

    @Test
    @DisplayName("convert a boolean to a jsonnode")
    void testOf() {
        boolean value = false;
        JsonBooleanNode node = JsonBooleanNode.of(value);
        String printedValue = node.compactString();

        assertThat(Boolean.parseBoolean(printedValue), is(value));
    }
}