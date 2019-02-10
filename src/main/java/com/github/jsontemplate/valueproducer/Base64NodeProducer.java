package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonStringNode;

import java.util.Base64;
import java.util.Map;

public class Base64NodeProducer extends AbstractNodeProducer<JsonStringNode> {

    public static final int DEFAULT_LENGTH = 12;

    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(() -> this.produceBase64(getDefaultLength()));
    }

    @Override
    public JsonStringNode produce(Map<String, String> paramMap) {
        Integer length = pickIntegerParam(paramMap, "length");

        return new JsonStringNode(() -> this.produceBase64(length));
    }

    private String produceBase64(int outputLength) {
        int originalLength = outputLength * 3 / 4;
        if (outputLength % 4 != 0) {
            originalLength += 1;
        }
        String originalString = new StringNodeProducer().produceString(originalLength);
        return Base64.getEncoder().encodeToString(originalString.getBytes());
    }

    protected int getDefaultLength() {
        return DEFAULT_LENGTH;
    }
}
