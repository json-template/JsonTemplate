package com.github.jsontemplate.valueproducer;

import org.junit.Test;

import java.util.Collections;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

public class IpNodeProducerTest {

    private static final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    private static Pattern VALID_IPV4_PATTERN = Pattern.compile(ipv4Pattern, Pattern.CASE_INSENSITIVE);

    private IpNodeProducer producer = new IpNodeProducer();

    @Test
    public void testProduce() {
        String producedValue = producer.produce().print();
        assertThat(VALID_IPV4_PATTERN.matcher(producedValue).find(), is(true));
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