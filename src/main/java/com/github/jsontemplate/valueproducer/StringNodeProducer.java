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

import com.github.jsontemplate.jsonbuild.JsonStringNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StringNodeProducer extends AbstractNodeProducer<JsonStringNode> {

    private final static String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final static int DEFAULT_LENGTH = 5;

    private Random random = new Random();
    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(() -> produceString(getDefaultLength()));
    }

    @Override
    public JsonStringNode produce(String value) {
        return new JsonStringNode(() -> value);
    }

    @Override
    public JsonStringNode produce(List<String> valueList) {
        return new JsonStringNode(() -> valueList.get(new Random().nextInt(valueList.size())));
    }

    @Override
    public JsonStringNode produce(Map<String, String> paramMap) {
        Map<String, String> copyParamMap = new HashMap<>(paramMap);

        Integer length = pickIntegerParam(copyParamMap, "length");
        Integer min = pickIntegerParam(copyParamMap, "min");
        Integer max = pickIntegerParam(copyParamMap, "max");

        validateParamMap(copyParamMap);

        if (length != null) {
            return new JsonStringNode(() -> produceString(length));
        } else if (min != null && max != null) {
            return new JsonStringNode(() -> produceString(randomIntInRange(min, max)));
        } else if (min != null) { // max == null
            return new JsonStringNode(() -> produceString(randomIntInRange(min, getDefaultMax(min))));
        } else if (max != null) { // min == null
            return new JsonStringNode(() -> produceString(randomIntInRange(getDefaultMin(max), max)));
        } else { // no expected parameters
            return produce();
        }
    }

    protected int getDefaultLength() {
        return DEFAULT_LENGTH;
    }

    protected int getDefaultMax(int min) {
        return 2 * min;
    }

    protected int getDefaultMin(int max) {
        return 0;
    }

    public String produceString(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHABETIC.length());
            chars[i] = ALPHABETIC.charAt(index);
        }
        return new String(chars);
    }
}
