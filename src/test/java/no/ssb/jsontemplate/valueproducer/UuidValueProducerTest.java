package no.ssb.jsontemplate.valueproducer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class UuidValueProducerTest {
    private UuidValueProducer producer = new UuidValueProducer();

    @Test
    @DisplayName("generates a random UUID string")
    void testProduce() {
        String producedValue = producer.produce().compactString();
        assertThat(producedValue, allOf(startsWith("\""), endsWith("\"")));
    }

    @Test
    @DisplayName("generates a UUID based on a UUID string")
    void testProduceWithSingleParam() {
        String fixedValue = UUID.randomUUID().toString();
        String producedValue = producer.produce(fixedValue).compactString();
        assertThat(producedValue, allOf(startsWith("\""), endsWith("\"")));
        assertThat(producedValue, is("\"" + fixedValue + "\""));
    }
}
