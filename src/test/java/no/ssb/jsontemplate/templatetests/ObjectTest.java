package no.ssb.jsontemplate.templatetests;

import com.jayway.jsonpath.DocumentContext;
import no.ssb.jsontemplate.JsonTemplate;
import org.junit.jupiter.api.Test;

import static no.ssb.jsontemplate.templatetests.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class ObjectTest {

    @Test
    void test_twoLevelObject() {
        DocumentContext document = parse(new JsonTemplate("{anObject:{aField:@s}}"));
        assertThat(document.read("$.anObject.aField", String.class), is(notNullValue()));
    }

    @Test
    void test_anObjectWithTwoFields() {
        DocumentContext document = parse(new JsonTemplate("{anObject:{aField:@s, bField:@s}}"));
        assertThat(document.read("$.anObject.aField", String.class), is(notNullValue()));
        assertThat(document.read("$.anObject.bField", String.class), is(notNullValue()));
    }

    @Test
    void test_multipleLevelObject() {
        DocumentContext document = parse(new JsonTemplate("{objA:{objB:{objC:{objD:{aField:@s}}}}}"));
        assertThat(document.read("$.objA.objB.objC.objD.aField", String.class), is(notNullValue()));
    }

    @Test
    void test_twoObjects() {
        DocumentContext document = parse(new JsonTemplate("{objA:{fieldA:@s},objB:{fieldB:@i}}"));
        assertThat(document.read("$.objA.fieldA", String.class), is(notNullValue()));
        assertThat(document.read("$.objB.fieldB", String.class), is(notNullValue()));
    }

}
