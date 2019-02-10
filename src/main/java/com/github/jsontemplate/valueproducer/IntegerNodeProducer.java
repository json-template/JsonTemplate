package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonIntegerNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class IntegerNodeProducer extends AbstractNodeProducer<JsonIntegerNode> {

    @Override
    public JsonIntegerNode produce() {
        return new JsonIntegerNode(() -> randomIntInRange(getDefaultMin(), getDefaultMax()));
    }

    @Override
    public JsonIntegerNode produce(String value) {
        int parsedInt = Integer.parseInt(value);
        return new JsonIntegerNode(() -> parsedInt);
    }

    @Override
    public JsonIntegerNode produce(List<String> valueList) {
        List<Integer> parsedValueList = valueList.stream().map(Integer::parseInt).collect(Collectors.toList());
        return new JsonIntegerNode(() -> parsedValueList.get(new Random().nextInt(parsedValueList.size())));
    }

    @Override
    public JsonIntegerNode produce(Map<String, String> paramMap) {
        Map<String, String> copyParamMap = new HashMap<>(paramMap);

        Integer min = pickIntegerParam(copyParamMap, "min");
        Integer max = pickIntegerParam(copyParamMap, "max");

        validateParamMap(copyParamMap);

        if (min != null && max != null && min < max) {
            return new JsonIntegerNode(() -> randomIntInRange(min, max));
        } else if (min != null && max == null) {
            return new JsonIntegerNode(() -> randomIntInRange(min, getDefaultMax(min)));
        } else if (min == null && max != null) {
            return new JsonIntegerNode(() -> randomIntInRange(getDefaultMin(max), max));
        } else {
            return produce();
        }
    }

    protected int getDefaultMax() {
        return 100;
    }

    protected int getDefaultMin() {
        return 0;
    }

    protected int getDefaultMax(int min) {
        return 2 * min;
    }

    protected int getDefaultMin(int max) {
        return 0;
    }

}
