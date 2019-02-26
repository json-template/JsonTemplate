package com.github.jsontemplate.main;

import com.github.jsontemplate.jsonbuild.JsonStringNode;
import com.github.jsontemplate.valueproducer.INodeProducer;
import com.github.jsontemplate.valueproducer.StringNodeProducer;
import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.github.jsontemplate.test.ParserUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class CustomProducerTest {

    @Test
    public void testWithNewProducer() {
        DocumentContext document = parse("{balance:@euro(20)}", new EuroProducer());
        assertThat(document.read("$.balance", String.class), is("€20"));
    }

    @Test
    public void testWithExtendedProducer() {
        DocumentContext document = parse("{ref:@s(GOF)}", new MyStringNodeProducer());
        assertThat(document.read("$.ref", String.class), is("[GOF]"));
    }

    private static class MyStringNodeProducer extends StringNodeProducer {

        @Override
        public JsonStringNode produce(String value) {
            return super.produce("[" + value + "]");
        }
    }

    private static class EuroProducer implements INodeProducer<JsonStringNode> {

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
