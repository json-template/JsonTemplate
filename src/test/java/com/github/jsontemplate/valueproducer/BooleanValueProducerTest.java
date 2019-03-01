package com.github.jsontemplate.valueproducer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BooleanValueProducerTest {

    private static final String STRING_TRUE = Boolean.TRUE.toString();
    private static final String STRING_FALSE = Boolean.FALSE.toString();
    private static final List<String> BOOLEAN_STRING_LIST = Arrays.asList(STRING_TRUE, STRING_FALSE);
    private BooleanValueProducer producer = new BooleanValueProducer();

    @Test
    @DisplayName("generates a random boolean string")
    void testProduce() {
        String producedValue = producer.produce().compactString();
        assertThat(producedValue, isIn(BOOLEAN_STRING_LIST));
    }

    @Test
    @DisplayName("generate a fixed boolean string")
    void testProduceWithSingleParam() {
        String producedValue = producer.produce(STRING_TRUE).compactString();
        assertThat(producedValue, is(STRING_TRUE));
    }

    @Test
    @DisplayName("generates false with an invalid parameter")
    void testProduceWithNonBooleanSingleParam() {
        String singleParam = "yes";
        String producedValue = producer.produce(singleParam).compactString();
        assertThat(producedValue, is("false"));
    }

    @Test
    @DisplayName("selects a value in a list of enumerated boolean strings")
    void testProduceWithListParam() {
        String producedValue = producer.produce(BOOLEAN_STRING_LIST).compactString();
        assertThat(producedValue, isIn(BOOLEAN_STRING_LIST));
    }

    @Test
    @DisplayName("not support - generate boolean string with a map parameter")
    void testProduceWithMapParam() {
        assertThrows(UnsupportedOperationException.class,
                () -> producer.produce(new HashMap<>()));
    }

}