package com.github.jsontemplate.valueproducer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Ipv6ValueProducerTest {

    private static final String ipv6Pattern = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";
    private static Pattern VALID_IPV6_PATTERN = Pattern.compile(ipv6Pattern, Pattern.CASE_INSENSITIVE);

    private Ipv6ValueProducer producer = new Ipv6ValueProducer();

    @Test
    @DisplayName("generates a random ipv6 string")
    void testProduce() {
        String producedValue = producer.produce().compactString();
        assertThat(VALID_IPV6_PATTERN.matcher(producedValue).find(), is(true));
    }

    @Test
    @DisplayName("not support - generates a ipv6 string with a single parameter")
    void testProduceWithSingleParam() {
        assertThrows(UnsupportedOperationException.class, () ->
                producer.produce("singleParam"));
    }

    @Test
    @DisplayName("not support - generates a ipv6 string with a list parameter")
    void testProduceWithListParam() {
        assertThrows(UnsupportedOperationException.class, () ->
                producer.produce(Collections.emptyList()));
    }

    @Test
    @DisplayName("not support - generates a ipv6 string with a map parameter")
    void testProduceWithMapParam() {
        assertThrows(UnsupportedOperationException.class, () ->
                producer.produce(Collections.emptyMap()));
    }
}