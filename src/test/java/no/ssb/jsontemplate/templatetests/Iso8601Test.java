package no.ssb.jsontemplate.templatetests;

import com.jayway.jsonpath.DocumentContext;
import no.ssb.jsontemplate.JsonTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static no.ssb.jsontemplate.templatetests.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class Iso8601Test {
    @Test
    void test_randomDateField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @iso8601}"));
        assertThat(document.read("$.aField", String.class), is(notNullValue()));

    }

    @ParameterizedTest
    @CsvSource(value = {
            "2021-01-01T00:00:00;yyyy-MM-ddThh:mi:ss;2021-01-01"
    }, delimiter = ';')
    void test_fixedUuidField(String date, String dateFormat, String expected) {
        DocumentContext document = parse(new JsonTemplate(String.format("{aField : @iso8601(date=%s, format=%s)}", date, dateFormat)));
        assertThat(document.read("$.aField", String.class), is(expected));
    }
}
