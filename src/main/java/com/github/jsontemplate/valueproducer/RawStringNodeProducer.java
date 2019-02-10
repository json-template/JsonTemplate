package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonRawStringNode;

public class RawStringNodeProducer extends AbstractNodeProducer<JsonRawStringNode> {

    @Override
    public JsonRawStringNode produce(String value) {
        return new JsonRawStringNode(() -> value);
    }
}
