package com.github.jsontemplate.main;

import com.github.jsontemplate.jsonbuild.JsonStringNode;
import com.github.jsontemplate.valueproducer.StringNodeProducer;
import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import static com.github.jsontemplate.test.TestUtils.parse;

public class CustomProducerTest {

    @Test
    public void test() {
        DocumentContext document = parse("{balance:@euro}", new EuroProducer());
    }

    @Test
    public void test2() {
        DocumentContext document = parse("{ref:@s(GOF)}", new MyStringNodeProducer());
    }

    private static class MyStringNodeProducer extends StringNodeProducer {

        @Override
        public JsonStringNode produce(String value) {
            return super.produce("[" + value + "]");
        }
    }
}
