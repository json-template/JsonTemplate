/*
 * Copyright 2019 the original author or authors.
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
import com.github.jsontemplate.jsonbuild.supplier.ListParamSupplier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class produces a {@link JsonStringNode JsonStringNode} which can generate json string value.
 */
public class StringValueProducer extends AbstractValueProducer<JsonStringNode> {

    /**
     * The type name used in the template, e.g. {aStringField: @s}
     */
    public static final String TYPE_NAME = "s";
    private static final String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int DEFAULT_LENGTH = 5;
    private static final int DEFAULT_MIN_LENGTH = 0;

    private Random random = new Random();

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate a random alphabetic string with length {@link #getDefaultLength()}.
     *
     * @return produced JsonStringNode
     */
    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(() -> produceString(getDefaultLength()));
    }

    /**
     * Produces a node which can generate a fixed string.
     *
     * @param value the fixed json string value
     * @return the produced json string node
     */
    @Override
    public JsonStringNode produce(String value) {
        return new JsonStringNode(() -> value);
    }

    /**
     * Produces a node which select a string in a list.
     *
     * @param valueList the enumerated string values
     * @return the produced json string node
     */
    @Override
    public JsonStringNode produce(List<String> valueList) {
        return new JsonStringNode(new ListParamSupplier<>(valueList));
    }

    /**
     * Produces a node which generate string based on a configuration.
     * <br>
     * Following parameters are currently supported:
     * <ul>
     * <li>length - the length of the generated string</li>
     * <li>min - the minimal length of the generated string,
     * if the maximal length is not given, it is 2 times greater
     * than the minimal length.
     * </li>
     * <li>max - the maximal length of the generated string,
     * if the minimal length is not given, it is 0
     * </li>
     * </ul>
     *
     * @param paramMap configuration
     * @return the produced json string node
     */
    @Override
    public JsonStringNode produce(Map<String, String> paramMap) {
        Map<String, String> copyParamMap = new HashMap<>(paramMap);

        Integer length = pickIntegerParam(copyParamMap, "length");
        Integer min = pickIntegerParam(copyParamMap, "min");
        Integer max = pickIntegerParam(copyParamMap, "max");

        validateParamMap(copyParamMap);

        if (length != null) {
            shouldBePositive(length, "length");
            return new JsonStringNode(() -> produceString(length));

        } else if (min != null && max != null) {
            shouldBePositive(min, "min");
            shouldBePositive(max, "max");
            shouldBeInAscOrder(min, max, "min", "max");
            return new JsonStringNode(() -> produceString(randomIntInRange(min, max)));

        } else if (min != null) {
            // max == null
            shouldBePositive(min, "min");
            return new JsonStringNode(() -> produceString(randomIntInRange(min, getDefaultMax(min))));

        } else if (max != null) {
            // min == null
            shouldBePositive(max, "max");
            return new JsonStringNode(() -> produceString(randomIntInRange(getDefaultMin(max), max)));

        } else { // no expected parameters
            return produce();
        }
    }

    /**
     * Returns the default length of the random string to be generated.
     *
     * @return the default length
     */
    protected int getDefaultLength() {
        return DEFAULT_LENGTH;
    }

    /**
     * Returns the default maximal length if it is not given in the map parameter.
     *
     * @param min the specified minimal bound
     * @return maximal bound
     */
    protected int getDefaultMax(int min) {
        return 2 * min;
    }

    /**
     * Returns the default minimal length is it is not given in the map parameter.
     *
     * @param max the specified maximal bound
     * @return minimal bound based on the given maximal bound
     */
    protected int getDefaultMin(int max) {
        return DEFAULT_MIN_LENGTH;
    }

    /**
     * Produces a random alphabetic string with a given length
     *
     * @param length the expected length of the string to be generated
     * @return a random string
     */
    public String produceString(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHABETIC.length());
            chars[i] = ALPHABETIC.charAt(index);
        }
        return new String(chars);
    }
}
