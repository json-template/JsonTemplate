package no.ssb.jsontemplate.valueproducer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Base64ValueProducerTest {

    private Base64ValueProducer producer = new Base64ValueProducer();

    @Test
    @DisplayName("generate a random base64 string")
    void produce() {
        String base64String = producer.produce().compactString();
        assertThat(base64String.length(), is(12 + 2));
    }

    @Test
    @DisplayName("not support - generate a fixed base64 string")
    void produceWithSingleParam() {
        assertThrows(UnsupportedOperationException.class,
                () -> producer.produce("singleParam"));
    }

    @Test
    @DisplayName("not support - select a base64 string from a list of enumerated values")
    void produceWithListParam() {
        List<String> emptyList = Collections.emptyList();
        assertThrows(UnsupportedOperationException.class, () -> producer.produce(emptyList));
    }

    @Test
    @DisplayName("generate a base64 string with the expected length which can be mod by 4")
    void produceWithParamLengthModBy4() {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("length", "40");
        String base64String = producer.produce(mapParam).compactString();
        int expectedLength = 40 + 2; // 2 for quotes
        assertThat(base64String.length(), is(expectedLength));
    }

    @Test
    @DisplayName("generate a base64 string with the expected length which can NOT be mod by 4")
    void produceWithMapLengthModNotBy4() {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("length", "41");
        String base64String = producer.produce(mapParam).compactString();
        int expectedLength = 44 + 2; // 2 for the quotes
        assertThat(base64String.length(), is(expectedLength));
    }
}