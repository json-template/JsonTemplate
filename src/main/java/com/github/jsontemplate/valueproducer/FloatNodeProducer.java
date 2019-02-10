package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonFloatNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class FloatNodeProducer extends AbstractNodeProducer<JsonFloatNode> {

    private static final float ZERO = 0f;

    @Override
    public JsonFloatNode produce() {
        return new JsonFloatNode(() -> new Random().nextFloat());
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
}
