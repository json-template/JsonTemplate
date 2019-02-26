package com.github.jsontemplate.jsonbuild;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class JsonIntegerNodeTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 20, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void testOf(int value) {
        JsonIntegerNode node = JsonIntegerNode.of(value);
        String printedValue = node.compactString();

        assertThat(Integer.parseInt(printedValue), is(value));
    }

}