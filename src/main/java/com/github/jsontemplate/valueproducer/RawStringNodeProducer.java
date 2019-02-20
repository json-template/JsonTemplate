package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonRawNode;

public class RawStringNodeProducer extends AbstractNodeProducer<JsonRawNode> {

    @Override
    public JsonRawNode produce(String value) {
        return new JsonRawNode(() -> value);
    }
}
