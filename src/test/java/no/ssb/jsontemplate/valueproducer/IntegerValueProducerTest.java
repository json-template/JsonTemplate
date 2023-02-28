package no.ssb.jsontemplate.valueproducer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntegerValueProducerTest {

    private IntegerValueProducer producer = new IntegerValueProducer();
    private int defaultMin = 0;
    private int defaultMax = 100;

    @Test
    void testProduce() {
        String producedValue = producer.produce().compactString();
        int producedInteger = Integer.parseInt(producedValue);
        assertThat(producedInteger, allOf(greaterThanOrEqualTo(defaultMin), lessThanOrEqualTo(defaultMax)));
    }

    @Test
    void testProduceWithSingleParam() {
        int myValue = 12345;
        String producedValue = producer.produce(Integer.toString(myValue)).compactString();
        int producedInteger = Integer.parseInt(producedValue);
        assertThat(producedInteger, is(myValue));
    }

    @Test
    @DisplayName("should fail if the given single param cannot be parsed")
    void testProduceWithInvalidSingleParam() {
        assertThrows(NumberFormatException.class, () -> producer.produce("myvalue"));
    }

    @Test
    @DisplayName("select a integeter string from a list of enumerated integer strings")
    void testProduceWithListParam() {
        List<Integer> intValues = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        List<String> stringValues = intValues.stream().map(Object::toString).collect(Collectors.toList());
        String producedValue = producer.produce(stringValues).compactString();
        assertThat(producedValue, isIn(stringValues));
    }

    @Test
    @DisplayName("generate a integer string with min parameter")
    void testProduceWithParamMin() {
        Map<String, String> mapParam = new HashMap<>();
        int min = 20;
        mapParam.put("min", Integer.toString(min));

        String producedValue = producer.produce(mapParam).compactString();
        int parsedInt = Integer.parseInt(producedValue);

        assertThat(parsedInt, greaterThanOrEqualTo(min));
    }

    @Test
    @DisplayName("generate a integer string with max parameter")
    void testProduceWithParamMax() {
        Map<String, String> mapParam = new HashMap<>();
        int max = 80;
        mapParam.put("max", Integer.toString(max));

        String producedValue = producer.produce(mapParam).compactString();
        int parsedInt = Integer.parseInt(producedValue);

        assertThat(parsedInt, lessThanOrEqualTo(max));
    }

    @Test
    @DisplayName("generate integer string with parameters min and max specified")
    void testProduceWithParamMinMax() {
        Map<String, String> mapParam = new HashMap<>();
        int min = 20;
        mapParam.put("min", Integer.toString(min));
        int max = 80;
        mapParam.put("max", Integer.toString(max));

        String producedValue = producer.produce(mapParam).compactString();
        int parsedInt = Integer.parseInt(producedValue);

        assertThat(parsedInt, allOf(greaterThanOrEqualTo(min), lessThanOrEqualTo(max)));
    }

    @Test
    void testProduceWithUnsupportedParameter() {
        assertThrows(IllegalArgumentException.class, () -> {
            Map<String, String> paramMap = new HashMap<>();

            int length = 9;
            paramMap.put("length", Integer.toString(length));

            producer.produce(paramMap).compactString();
        });
    }
}