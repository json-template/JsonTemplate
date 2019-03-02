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

import com.github.jsontemplate.jsonbuild.JsonFloatNode;
import com.github.jsontemplate.jsonbuild.supplier.ListParamSupplier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class produces a {@link JsonFloatNode JsonFloatNode} which can generate json numeric(float) value.
 */
public class FloatValueProducer extends AbstractValueProducer<JsonFloatNode> {

    /**
     * The type name used in the template, e.g. {aFloatField: @f}
     */
    public static final String TYPE_NAME = "f";
    private static final float ONE_HUNDRED = 100f;

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate a random float.
     * The range of the integer is obtained from
     * {@link #getDefaultRange()}
     * By default, the range is 0 to 100.
     *
     * @return produced JsonFloatNode
     */
    @Override
    public JsonFloatNode produce() {
        return new JsonFloatNode(() -> new Random().nextFloat() * getDefaultRange());
    }

    /**
     * Produces a node which can generate a fixed float.
     *
     * @param value the string representation of the float
     * @return the produced json float node
     */
    @Override
    public JsonFloatNode produce(String value) {
        float parsedFloat = Float.parseFloat(value);
        return new JsonFloatNode(() -> parsedFloat);
    }

    /**
     * Produces a node which selects a string in a list.
     * The selected string is parsed to an float.
     *
     * @param valueList the enumerated string values
     * @return the produced json float node
     */
    @Override
    public JsonFloatNode produce(List<String> valueList) {
        List<Float> parsedValueList = valueList.stream().map(Float::parseFloat).collect(Collectors.toList());
        return new JsonFloatNode(new ListParamSupplier<>(parsedValueList));
    }

    /**
     * Produces a node which generates an float based on a configuration.
     * <br>
     * Following parameters are currently supported:
     * <ul>
     * <li>min - the minimal value of the generated integer,
     * if the maximal value is not given, it is returned from
     * {@link #getDefaultMax(float) getDefaultMax(float)} which is 2 times
     * greater than the minimal length.
     * </li>
     * <li>max - the maximal length of the generated string,
     * if the minimal value is not given, it is returned from
     * {@link #getDefaultMin(float) getDefaultMin(float)} which is 0
     * </li>
     * </ul>
     *
     * @param paramMap configuration
     * @return the produced json float node
     */
    @Override
    public JsonFloatNode produce(Map<String, String> paramMap) {
        Map<String, String> copyParamMap = new HashMap<>(paramMap);

        Float min = pickFloatParam(copyParamMap, "min");
        Float max = pickFloatParam(copyParamMap, "max");

        validateParamMap(copyParamMap);

        if (min != null && max != null) {
            shouldBeInAscOrder(min, max, "min", "max");
            return new JsonFloatNode(() -> randomFloatInRange(min, max));
        } else if (min != null) {
            return new JsonFloatNode(() -> randomFloatInRange(min, getDefaultMax(min)));
        } else if (max != null) {
            return new JsonFloatNode(() -> randomFloatInRange(getDefaultMin(max), max));
        } else {
            return produce();
        }
    }

    /**
     * Returns the default maximal bound if the minimal bound is not given in
     * the map parameter.
     *
     * @param min the specified minimal bound
     * @return maximal bound based on the given minimal bound
     */
    protected float getDefaultMax(float min) {
        return min + ONE_HUNDRED;
    }

    /**
     * Returns the default minimal bound if the maximal bound is not given in
     * the map parameter.
     *
     * @param max the specified maximal bound
     * @return minimal bound based on the given maximal bound
     */
    protected float getDefaultMin(float max) {
        return max - ONE_HUNDRED;
    }

    /**
     * Returns the default maximal bound of the default range.
     *
     * @return the default range
     */
    protected float getDefaultRange() {
        return ONE_HUNDRED;
    }
}
