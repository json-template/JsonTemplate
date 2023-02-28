package no.ssb.jsontemplate.templatetests;

import no.ssb.jsontemplate.JsonTemplate;
import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RawJsonTest {

    @Test
    void test_rawStringJson() {
        String rawContent = "{\n" +
                "  \"jsonField\" : \"C\"\n" +
                "}";

        DocumentContext document = TestUtils.parse(new JsonTemplate("{aField : @raw(`" + rawContent + "`)}"));
        assertThat(document.read("$.aField.jsonField", String.class), is("C"));
    }

    @Test
    void test_rawStringJsonWithVariable() {
        String rawContent = "{\n" +
                "  \"jsonField\" : \"C\"\n" +
                "}";

        DocumentContext document = TestUtils.parse(
                new JsonTemplate("{aField : @raw($jsonContent)}")
                        .withVar("jsonContent", rawContent));
        assertThat(document.read("$.aField.jsonField", String.class), is("C"));
    }

}
