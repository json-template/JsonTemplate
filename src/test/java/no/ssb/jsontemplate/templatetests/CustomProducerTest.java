package no.ssb.jsontemplate.templatetests;

import com.jayway.jsonpath.DocumentContext;
import no.ssb.jsontemplate.JsonTemplate;
import no.ssb.jsontemplate.jsonbuild.JsonStringNode;
import no.ssb.jsontemplate.valueproducer.IValueProducer;
import no.ssb.jsontemplate.valueproducer.StringValueProducer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static no.ssb.jsontemplate.templatetests.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class CustomProducerTest {

    @Test
    void testWithNewProducer() {
        JsonTemplate jsonTemplate = new JsonTemplate("{balance:@euro(20)}").withValueProducer(new EuroProducer());
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.balance", String.class), is("€20"));
    }

    @Test
    void testWithExtendedProducer() {
        JsonTemplate jsonTemplate = new JsonTemplate("{ref:@s(GOF)}").withValueProducer(new MyStringValueProducer());
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.ref", String.class), is("[GOF]"));
    }

    private static class MyStringValueProducer extends StringValueProducer {

        @Override
        public JsonStringNode produce(String value) {
            return super.produce("[" + value + "]");
        }
    }

    private static class EuroProducer implements IValueProducer<JsonStringNode> {

        @Override
        public String getTypeName() {
            return "euro";
        }

        @Override
        public JsonStringNode produce() {
            return new JsonStringNode(() -> "€" + new Random().nextInt(100));
        }

        @Override
        public JsonStringNode produce(String value) {
            return new JsonStringNode(() -> "€" + value);
        }

        @Override
        public JsonStringNode produce(List<String> valueList) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonStringNode produce(Map<String, String> paramMap) {
            throw new UnsupportedOperationException();
        }
    }
}
