package com.github.jsontemplate.jsonbuild;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class JsonFloatNodeTest {

    @ParameterizedTest
    @ValueSource(floats = {0f, -1f, 1.23456f, 20f, Float.MAX_VALUE, Float.MIN_VALUE})
    void testOf(float value) {
        JsonFloatNode node = JsonFloatNode.of(value);
        String printedValue = node.print();

        assertThat(Float.parseFloat(printedValue), is(value));
    }

}