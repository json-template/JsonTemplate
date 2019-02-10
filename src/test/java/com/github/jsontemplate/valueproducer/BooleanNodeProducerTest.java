package com.github.jsontemplate.valueproducer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(JUnit4.class)
public class BooleanNodeProducerTest {

    private BooleanNodeProducer producer = new BooleanNodeProducer();

    @Test
    public void testProduce() {
        String producedValue = producer.produce().print();
        assertThat(producedValue, isIn(Arrays.asList("true", "false")));
    }

    @Test
    public void testProduceWithSingleParam() {
        String singleParam = "true";
        String producedValue = producer.produce(singleParam).print();
        assertThat(producedValue, is(singleParam));
    }

    @Test
    public void testProduceWithNonBooleanSingleParam() {
        String singleParam = "yes";
        String producedValue = producer.produce(singleParam).print();
        assertThat(producedValue, is("false"));
    }

    @Test
    public void testProduceWithListParam() {
        List<String> strings = Arrays.asList("true", "false");
        String producedValue = producer.produce(strings).print();
        assertThat(producedValue, isIn(strings));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProduceWithMapParam() {
        producer.produce(new HashMap<>());
    }
}