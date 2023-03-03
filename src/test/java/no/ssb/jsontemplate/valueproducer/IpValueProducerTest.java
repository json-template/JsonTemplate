package no.ssb.jsontemplate.valueproducer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IpValueProducerTest {

    private static final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    private static final Pattern VALID_IPV4_PATTERN = Pattern.compile(ipv4Pattern, Pattern.CASE_INSENSITIVE);

    private final IpValueProducer producer = new IpValueProducer();

    @Test
    @DisplayName("generates a random ipv4 string")
    void testProduce() {
        String producedValue = producer.produce().compactString();
        assertThat(VALID_IPV4_PATTERN.matcher(producedValue).find(), is(true));
    }

    @Test
    @DisplayName("not support - generates a ipv4 string with a single parameter")
    void testProduceWithSingleParam() {
        assertThrows(UnsupportedOperationException.class, () -> producer.produce("singleParam"));
    }

    @Test
    @DisplayName("not support - generates a ipv4 string with a list parameter")
    void testProduceWithListParam() {
        List<String> emptyList = Collections.emptyList();
        assertThrows(UnsupportedOperationException.class, () -> producer.produce(emptyList));
    }

    @Test
    @DisplayName("not support - generates a ipv4 string with a map parameter")
    void testProduceWithMapParam() {
        Map<String, String> emptyMap = Collections.emptyMap();
        assertThrows(UnsupportedOperationException.class, () -> producer.produce(emptyMap));
    }
}