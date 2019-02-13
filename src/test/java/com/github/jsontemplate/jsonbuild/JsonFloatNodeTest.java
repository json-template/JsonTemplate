package com.github.jsontemplate.jsonbuild;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class JsonFloatNodeTest {

    @Test
    void testOf() {
        float value = 1.234f;
        JsonFloatNode node = JsonFloatNode.of(value);
        String printedValue = node.print();

        assertThat(Float.parseFloat(printedValue), is(value));
    }

}