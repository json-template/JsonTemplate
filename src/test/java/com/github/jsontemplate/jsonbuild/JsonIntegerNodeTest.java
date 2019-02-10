package com.github.jsontemplate.jsonbuild;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(JUnit4.class)
public class JsonIntegerNodeTest {

    @Test
    public void testOf() {
        int value = 20;
        JsonIntegerNode node = JsonIntegerNode.of(value);
        String printedValue = node.print();

        assertThat(Integer.parseInt(printedValue), is(value));
    }

}