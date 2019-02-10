package com.github.jsontemplate.valueproducer;

import org.junit.Test;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;

public class Base64NodeProducerTest {

    private Base64NodeProducer producer = new Base64NodeProducer();

    @Test
    public void produce() {
        String base64String = producer.produce().print();
        assertThat(base64String.length(), is(12 + 2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void produceWithSingleParam() {
        producer.produce("singleParam").print();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void produceWithListParam() {
        producer.produce(Collections.emptyList()).print();
    }

    @Test
    public void produceWithParamLengthModBy4() {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("length", "40");
        String base64String = producer.produce(mapParam).print();
        int expectedLength = 40 + 2; // 2 for quotes
        assertThat(base64String.length(), is(expectedLength));
    }

    @Test
    public void produceWithMapLengthModNotBy4() {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("length", "41");
        String base64String = producer.produce(mapParam).print();
        int expectedLength = 44 + 2; // 2 for the quotes
        assertThat(base64String.length(), is(expectedLength));
    }
}