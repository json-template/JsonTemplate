package no.ssb.jsontemplate.templatetests;

import com.jayway.jsonpath.DocumentContext;
import no.ssb.jsontemplate.JsonTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static no.ssb.jsontemplate.templatetests.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UuidTest {
    private final String uuidPattern = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    @Test
    void test_randomUuidField() {
        DocumentContext document = parse(new JsonTemplate("{aField : @uuid}"));
        assertThat(document.read("$.aField", String.class), is(notNullValue()));
        assertThat(document.read("$.aField", String.class), matchesRegex(uuidPattern));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "895ab430-b9d9-11ed-afa1-0242ac120002",
            "000003e8-b9d9-21ed-ac00-325096b39f47",
            "f4387906-4ccb-3360-a682-f692cefd578c",
            "bcc18262-90f0-4819-87b3-8616cee1f48a",
            "fdbb17ca-a56e-553c-af16-ade3976bcae4"
    })
    void test_fixedUuidField(String candidate) {
        DocumentContext document = parse(new JsonTemplate(String.format("{aField : @uuid(%s)}", candidate)));
        assertThat(document.read("$.aField", String.class), is(candidate));
        assertThat(document.read("$.aField", String.class), matchesRegex(uuidPattern));
    }
}
