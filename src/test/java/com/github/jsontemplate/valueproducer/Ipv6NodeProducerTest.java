package com.github.jsontemplate.valueproducer;

import org.junit.Test;

import java.util.Collections;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Ipv6NodeProducerTest {

    private static final String ipv6Pattern = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";
    private static Pattern VALID_IPV6_PATTERN = Pattern.compile(ipv6Pattern, Pattern.CASE_INSENSITIVE);

    private Ipv6NodeProducer producer = new Ipv6NodeProducer();

    @Test
    public void testProduce() {
        String producedValue = producer.produce().print();
        assertThat(VALID_IPV6_PATTERN.matcher(producedValue).find(), is(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProduceWithSingleParam() {
        producer.produce("singleParam");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProduceWithListParam() {
        producer.produce(Collections.emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProduceWithMapParam() {
        producer.produce(Collections.emptyMap());
    }
}