package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonBooleanNode;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BooleanNodeProducer extends AbstractNodeProducer<JsonBooleanNode> {

    @Override
    public JsonBooleanNode produce() {
        return new JsonBooleanNode(() -> new Random().nextBoolean());
    }

    @Override
    public JsonBooleanNode produce(String value) {
        boolean parsedBoolean = Boolean.parseBoolean(value);
        return new JsonBooleanNode(() -> parsedBoolean);
    }

    @Override
    public JsonBooleanNode produce(List<String> valueList) {
        List<Boolean> parsedValueList = valueList.stream().map(Boolean::parseBoolean).collect(Collectors.toList());
        return new JsonBooleanNode(() -> parsedValueList.get(new Random().nextInt(parsedValueList.size())));
    }
}
