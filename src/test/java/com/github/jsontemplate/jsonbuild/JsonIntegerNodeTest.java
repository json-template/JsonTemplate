package com.github.jsontemplate.jsonbuild;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class JsonIntegerNodeTest {

    @Test
    void testOf() {
        int value = 20;
        JsonIntegerNode node = JsonIntegerNode.of(value);
        String printedValue = node.print();

        assertThat(Integer.parseInt(printedValue), is(value));
    }

}