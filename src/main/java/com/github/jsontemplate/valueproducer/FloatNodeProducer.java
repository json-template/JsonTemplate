package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonFloatNode;
import com.github.jsontemplate.jsonbuild.JsonIntegerNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class FloatNodeProducer extends AbstractNodeProducer<JsonFloatNode> {

    private static final float ZERO = 0f;
    private static final int DEFAULT_RANGE = 100;

    @Override
    public JsonFloatNode produce() {
        return new JsonFloatNode(() -> new Random().nextFloat() * getDefaultRange());
    }

    @Override
    public JsonFloatNode produce(String value) {
        float parsedFloat = Float.parseFloat(value);
        return new JsonFloatNode(() -> parsedFloat);
    }

    @Override
    public JsonFloatNode produce(List<String> valueList) {
        List<Float> parsedValueList = valueList.stream().map(Float::parseFloat).collect(Collectors.toList());
        return new JsonFloatNode(() -> parsedValueList.get(new Random().nextInt(parsedValueList.size())));
    }

    @Override
    public JsonFloatNode produce(Map<String, String> paramMap) {
        Map<String, String> copyParamMap = new HashMap<>(paramMap);

        Float min = pickFloatParam(copyParamMap, "min");
        Float max = pickFloatParam(copyParamMap, "max");

        validateParamMap(copyParamMap);

        if (min != null && max != null && min < max) {
            return new JsonFloatNode(() -> randomFloatInRange(min, max));
        } else if (min != null && max == null) {
            return new JsonFloatNode(() -> randomFloatInRange(min, getDefaultMax(min)));
        } else if (min == null && max != null) {
            return new JsonFloatNode(() -> randomFloatInRange(getDefaultMin(max), max));
        } else {
            return produce();
        }
    }

    protected float getDefaultMax(float min) {
        return 2 * min;
    }

    protected float getDefaultMin(float max) {
        return ZERO;
    }

    protected float getDefaultRange() {
        return DEFAULT_RANGE;
    }
}
