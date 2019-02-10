package com.github.jsontemplate.valueproducer;


import com.github.jsontemplate.jsonbuild.JsonStringNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

@RunWith(JUnit4.class)
public class StringNodeProducerTest {

    private StringNodeProducer producer = new StringNodeProducer();
    private int defaultLength = 5;

    @Test
    public void testProduce() {
        String producedValue = producer.produce().print();
        assertThat(producedValue, allOf(startsWith("\""), endsWith("\"")));
        assertThat(producedValue.length(), is(defaultLength + 2));
    }

    @Test
    public void testProduceWithSingleParam() {
        String fixedValue = "myValue";
        String producedValue = producer.produce(fixedValue).print();
        assertThat(producedValue, is("\"" + fixedValue + "\""));
    }

    @Test
    public void testProduceWithListParam() {
        List<String> strings = Arrays.asList("A", "B", "C");
        String producedValue = producer.produce(strings).print();
        assertThat(producedValue, isIn(Arrays.asList("\"A\"", "\"B\"", "\"C\"")));
    }

    @Test
    public void testProduceWithParamSize() {
        Map<String, String> paramMap = new HashMap<>();

        int size = 11;
        paramMap.put("size", Integer.toString(size));

        String producedValue = producer.produce(paramMap).print();
        assertThat(producedValue, allOf(startsWith("\""), endsWith("\"")));
        assertThat(producedValue.length(), is(size + 2));
    }

    @Test
    public void testProduceWithParamMin() {
        Map<String, String> paramMap = new HashMap<>();

        int min = 11;
        paramMap.put("min", Integer.toString(min));

        String producedValue = producer.produce(paramMap).print();
        assertThat(producedValue, allOf(startsWith("\""), endsWith("\"")));
        assertThat(producedValue.length(), greaterThanOrEqualTo(min + 2));
    }

    @Test
    public void testProduceWithParamMax() {
        Map<String, String> paramMap = new HashMap<>();

        int max = 11;
        paramMap.put("max", Integer.toString(max));

        String producedValue = producer.produce(paramMap).print();
        assertThat(producedValue, allOf(startsWith("\""), endsWith("\"")));
        assertThat(producedValue.length(), lessThanOrEqualTo(max + 2));
    }

    @Test
    public void testProduceWithParamMinMax() {
        Map<String, String> paramMap = new HashMap<>();

        int min = 6;
        int max = 11;
        paramMap.put("min", Integer.toString(min));
        paramMap.put("max", Integer.toString(max));

        String producedValue = producer.produce(paramMap).print();
        assertThat(producedValue, allOf(startsWith("\""), endsWith("\"")));
        assertThat(producedValue.length(), allOf(greaterThanOrEqualTo(min + 2), lessThanOrEqualTo(max + 2)));
    }

    @Test
    public void testProduceWithParamSizeMinMax() {
        Map<String, String> paramMap = new HashMap<>();

        int size = 20;
        int min = 6;
        int max = 11;
        paramMap.put("size", Integer.toString(size));
        paramMap.put("min", Integer.toString(min));
        paramMap.put("max", Integer.toString(max));

        String producedValue = producer.produce(paramMap).print();
        assertThat(producedValue, allOf(startsWith("\""), endsWith("\"")));
        assertThat(producedValue.length(), is(size + 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProduceWithUnsupportedParameter() {
        Map<String, String> paramMap = new HashMap<>();

        int length = 9;
        paramMap.put("length", Integer.toString(length));

        producer.produce(paramMap).print();
    }
}