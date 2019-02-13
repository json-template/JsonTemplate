package com.github.jsontemplate.jsonbuild;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class JsonStringNodeTest {

    @Test
    void testOf() {
        String value = "abced";
        JsonStringNode node = JsonStringNode.of(value);
        String printedValue = node.print();

        assertThat(printedValue, is("\"" + value + "\""));
    }
}