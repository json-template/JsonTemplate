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
import com.github.jsontemplate.jsonbuild.supplier.ListParamSupplier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class produces a {@link JsonIntegerNode JsonIntegerNode} which can generate json numeric(integer) value.
 */
public class IntegerValueProducer extends AbstractValueProducer<JsonIntegerNode> {

    /**
     * The type name used in the template, e.g. {anIntegerField: @i}
     */
    public static final String TYPE_NAME = "i";
    private static final int ZERO = 0;
    private static final int ONE_HUNDRED = 100;

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate a random integer.
     * The range of the integer is obtained from
     * {@link #getDefaultMin() getDefaultMin()} and {@link #getDefaultMax() getDefaultMax()}
     * By default, the range is 0 to 100.
     *
     * @return produced JsonIntegerNode
     */
    @Override
    public JsonIntegerNode produce() {
        return new JsonIntegerNode(() -> randomIntInRange(getDefaultMin(), getDefaultMax()));
    }

    /**
     * Produces a node which can generate a fixed integer.
     *
     * @param value the string representation of the integer
     * @return
     */
    @Override
    public JsonIntegerNode produce(String value) {
        int parsedInt = Integer.parseInt(value);
        return new JsonIntegerNode(() -> parsedInt);
    }

    /**
     * Produces a node which selects a string in a list.
     * The selected string is parsed to an integer.
     *
     * @param valueList the enumerated string values
     * @return
     */
    @Override
    public JsonIntegerNode produce(List<String> valueList) {
        List<Integer> parsedValueList = valueList.stream().map(Integer::parseInt).collect(Collectors.toList());
        return new JsonIntegerNode(new ListParamSupplier<>(parsedValueList));
    }

    /**
     * Produces a node which generates an integer based on a configuration.
     * <br/>
     * Following parameters are currently supported:
     * <ul>
     * <li>min - the minimal value of the generated integer,
     * if the maximal value is not given, it is returned from
     * {@link #getDefaultMax(int) getDefaultMax(int)} which is 2 times
     * greater than the minimal length.
     * </li>
     * <li>max - the maximal length of the generated string,
     * if the minimal value is not given, it is returned from
     * {@link #getDefaultMin(int) getDefaultMin(int)} which is 0
     * </li>
     * <ul/>
     *
     * @param paramMap configuration
     * @return
     */
    @Override
    public JsonIntegerNode produce(Map<String, String> paramMap) {
        Map<String, String> copyParamMap = new HashMap<>(paramMap);

        Integer min = pickIntegerParam(copyParamMap, "min");
        Integer max = pickIntegerParam(copyParamMap, "max");

        validateParamMap(copyParamMap);

        if (min != null && max != null) {
            shouldBeInAscOrder(min, max, "min", "max");
            return new JsonIntegerNode(() -> randomIntInRange(min, max));
        } else if (min != null) {
            return new JsonIntegerNode(() -> randomIntInRange(min, getDefaultMax(min)));
        } else if (max != null) {
            return new JsonIntegerNode(() -> randomIntInRange(getDefaultMin(max), max));
        } else {
            return produce();
        }
    }

    /**
     * Returns the default maximal bound of the default range.
     *
     * @return
     */
    protected int getDefaultMax() {
        return ONE_HUNDRED;
    }

    /**
     * Returns the default mininal bound of the default range.
     *
     * @return
     */
    protected int getDefaultMin() {
        return ZERO;
    }

    /**
     * Returns the default maximal bound if the minimal bound is not given in
     * the map parameter.
     *
     * @param min the specified minimal bound
     * @return maximal bound based on the given minimal bound
     */
    protected int getDefaultMax(int min) {
        return min + ONE_HUNDRED;
    }

    /**
     * Returns the default minimal bound if the maximal bound is not given in
     * the map parameter.
     *
     * @param max the specified maximal bound
     * @return minimal bound based on the given maximal bound
     */
    protected int getDefaultMin(int max) {
        return max - ONE_HUNDRED;
    }

}
