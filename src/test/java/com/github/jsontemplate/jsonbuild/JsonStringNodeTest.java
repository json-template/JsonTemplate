package com.github.jsontemplate.jsonbuild;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class JsonStringNodeTest {

    @Test
    public void testOf() {
        String value = "abced";
        JsonStringNode node = JsonStringNode.of(value);
        String printedValue = node.print();

        assertThat(printedValue, is("\"" + value + "\""));
    }
}