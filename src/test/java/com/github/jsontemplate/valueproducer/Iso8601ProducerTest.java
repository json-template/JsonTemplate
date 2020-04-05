package com.github.jsontemplate.valueproducer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Iso8601ProducerTest {
    private Iso8601ValueProducer producer = new Iso8601ValueProducer();
    @Test
    @DisplayName("Generate an ISO8601 timestamp")
    void testProduce() {
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
