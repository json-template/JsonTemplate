package no.ssb.jsontemplate.valueproducer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class Iso8601ProducerTest {

    private final Iso8601ValueProducer producer = new Iso8601ValueProducer();

    @Test
    @DisplayName("Generate an ISO8601 timestamp")
    void testProduce() {
        String producedValue = producer.produce().compactString();
        System.out.println(producedValue);

        Exception ex = null;
        try {
            DateTimeFormatter.ISO_DATE_TIME.parse(producedValue.substring(1, producedValue.length() - 1));
        } catch (DateTimeParseException e) {
            ex = e;
        }

        assertThat(ex, nullValue());
    }

    @Test
    @DisplayName("Generate an ISO8601 timestamp")
    void testProduceDateString() {
        String producedValue = producer.produce().compactString();
        System.out.println(producedValue);

        Exception ex = null;
        try {
            DateTimeFormatter.ISO_DATE_TIME.parse(producedValue.substring(1, producedValue.length() - 1));
        } catch (DateTimeParseException e) {
            ex = e;
        }

        assertThat(ex, nullValue());
    }
}
