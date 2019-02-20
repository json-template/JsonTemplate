/*
 * Copyright 2019 Haihan Yin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonIntegerNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class IntegerNodeProducer extends AbstractNodeProducer<JsonIntegerNode> {

    private static final int ZERO = 0;

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
        return ZERO;
    }

}
