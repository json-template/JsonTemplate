package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonStringNode;

import java.util.Base64;

public class Base64NodeProducer extends AbstractNodeProducer<JsonStringNode> {

    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceBase64);
    }

    private String produceBase64() {
        byte[] bytes = Long.toString(System.currentTimeMillis()).getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }
}
