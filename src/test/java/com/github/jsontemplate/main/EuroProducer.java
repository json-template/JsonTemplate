package com.github.jsontemplate.main;

import com.github.jsontemplate.jsonbuild.JsonIntegerNode;
import com.github.jsontemplate.jsonbuild.JsonStringNode;
import com.github.jsontemplate.valueproducer.INodeProducer;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class EuroProducer implements INodeProducer<JsonStringNode> {

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
