package no.ssb.jsontemplate.valueproducer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;;

public class ConcatStringProducerTest {
    private ConcatStringProducer producer = new ConcatStringProducer();

    @Test
    @DisplayName("Generate an empty string")
    void testProduce() {
        String producedValue = producer.produce().compactString();
        assertThat(producedValue, equalTo("\"\""));
    }

    @Test
    @DisplayName("Generate a string with input")
    void testProduceWithSingleParam() {
        String producedValue = producer.produce("Hi!").compactString();
        assertThat(producedValue, is("\"" + "Hi!" + "\""));
    }

    @Test
    @DisplayName("Concatenate a list of strings")
    void testProduceWithListParam() {
        List<String> paramList = Arrays.asList("Hello", " ", "world", "!");
        String producedValue = producer.produce(paramList).compactString();
        assertThat(producedValue, equalTo("\"" +  "Hello world!" + "\""));
    }
}
