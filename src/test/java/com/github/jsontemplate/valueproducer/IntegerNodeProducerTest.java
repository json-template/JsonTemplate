package com.github.jsontemplate.valueproducer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(JUnit4.class)
public class IntegerNodeProducerTest {

    private IntegerNodeProducer producer = new IntegerNodeProducer();
    private int defaultMin = 0;
    private int defaultMax = 100;

    @Test
    public void testProduce() {
        String producedValue = producer.produce().print();
        int producedInteger = Integer.parseInt(producedValue);
        assertThat(producedInteger, allOf(greaterThanOrEqualTo(defaultMin), lessThanOrEqualTo(defaultMax)));
    }

    @Test
    public void testProduceWithSingleParam() {
        int myValue = 12345;
        String producedValue = producer.produce(Integer.toString(myValue)).print();
        int producedInteger = Integer.parseInt(producedValue);
        assertThat(producedInteger, is(myValue));
    }

    @Test(expected = NumberFormatException.class)
    public void testProduceWithInvalidSingleParam() {
        producer.produce("myvalue");
    }

    @Test
    public void testProduceWithListParam() {
        List<Integer> intValues = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        List<String> stringValues = intValues.stream().map(Object::toString).collect(Collectors.toList());
        String producedValue = producer.produce(stringValues).print();
        assertThat(producedValue, isIn(stringValues));
    }

    @Test
    public void testProduceWithParamMin() {
        Map<String, String> mapParam = new HashMap<>();
        int min = 20;
        mapParam.put("min", Integer.toString(min));

        String producedValue = producer.produce(mapParam).print();
        int parsedInt = Integer.parseInt(producedValue);

        assertThat(parsedInt, greaterThanOrEqualTo(min));
    }

    @Test
    public void testProduceWithParamMax() {
        Map<String, String> mapParam = new HashMap<>();
        int max = 80;
        mapParam.put("max", Integer.toString(max));

        String producedValue = producer.produce(mapParam).print();
        int parsedInt = Integer.parseInt(producedValue);

        assertThat(parsedInt, lessThanOrEqualTo(max));
    }

    @Test
    public void testProduceWithParamMinMax() {
        Map<String, String> mapParam = new HashMap<>();
        int min = 20;
        mapParam.put("min", Integer.toString(min));
        int max = 80;
        mapParam.put("max", Integer.toString(max));

        String producedValue = producer.produce(mapParam).print();
        int parsedInt = Integer.parseInt(producedValue);

        assertThat(parsedInt, allOf(greaterThanOrEqualTo(min), lessThanOrEqualTo(max)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProduceWithUnsupportedParameter() {
        Map<String, String> paramMap = new HashMap<>();

        int length = 9;
        paramMap.put("length", Integer.toString(length));

        producer.produce(paramMap).print();
    }
}