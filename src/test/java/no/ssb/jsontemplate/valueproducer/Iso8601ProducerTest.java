package no.ssb.jsontemplate.valueproducer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class Iso8601ProducerTest {
    @Test
    @DisplayName("Generate an ISO8601 timestamp")
    void testProduce() {
        Iso8601ValueProducer producer = new Iso8601ValueProducer();
        String producedValue = producer.produce().compactString();

        Exception ex = null;
        try {
            DateTimeFormatter.ISO_DATE_TIME.parse(producedValue.substring(1, producedValue.length() - 1));
        } catch (DateTimeParseException e) {
            ex = e;
        }

        assertThat(ex, nullValue());
    }
}
