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

import com.github.jsontemplate.jsonbuild.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractValueProducer<T extends JsonNode> implements IValueProducer<T> {

    @Override
    public T produce() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T produce(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T produce(List<String> valueList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T produce(Map<String, String> paramMap) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the integer value from the paramMap based on the paramName.
     * The value is then removed from the map.
     *
     * @param paramMap parameter map
     * @param paramName name of the value
     * @return the integer value referred by the paramName
     */
    protected Integer pickIntegerParam(Map<String, String> paramMap, String paramName) {
        return pickParamValue(paramMap, paramName, Integer::parseInt);
    }

    /**
     * Returns the float value from the paramMap based on the paramName.
     * The value is then removed from the map.
     *
     * @param paramMap parameter map
     * @param paramName name of the value
     * @return the float value referred by the paramName
     */
    protected Float pickFloatParam(Map<String, String> paramMap, String paramName) {
        return pickParamValue(paramMap, paramName, Float::parseFloat);
    }

    /**
     * Returns the boolean value from the paramMap based on the paramName.
     * The value is then removed from the map.
     *
     * @param paramMap parameter map
     * @param paramName name of the value
     * @return the boolean value referred by the paramName
     */
    protected Boolean pickBooleanParam(Map<String, String> paramMap, String paramName) {
        return pickParamValue(paramMap, paramName, Boolean::parseBoolean);
    }

    /**
     * Returns the string value from the paramMap based on the paramName.
     * The value is then removed from the map.
     *
     * @param paramMap parameter map
     * @param paramName name of the value
     * @return the string value referred by the paramName
     */
    protected String pickStringParam(Map<String, String> paramMap, String paramName) {
        return paramMap.get(paramName);
    }

    private <R> R pickParamValue(Map<String, String> paramMap, String paramName, Function<String, R> parser) {
        String paramValue = paramMap.remove(paramName);
        if (paramValue != null) {
            return parser.apply(paramValue);
        } else {
            return null;
        }
    }

    /**
     * Returns a random integer in the range of min and max.
     *
     * @param min minimal bound
     * @param max maximal bound
     * @return random value between min and max
     */
    protected int randomIntInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Returns a random float in the range of min and max.
     *
     * @param min minimal bound
     * @param max maximal bound
     * @return random value between min and max
     */
    protected float randomFloatInRange(float min, float max) {
        return min + new Random().nextFloat() * (max - min);
    }

    /**
     * Validates if the map has values. If it has, that means it contains
     * values which are not supported. In this case, an
     * {@link IllegalArgumentException IllegalArgumentExcpetion} is thrown.
     *
     * @param paramMap parameter map
     */
    protected void validateParamMap(Map<String, String> paramMap) {
        if (paramMap.size() > 0) {
            String unexpectedArgument = paramMap.keySet().stream().collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Arguments [" + unexpectedArgument + "] is not supported in " + this.getClass().getName());
        }
    }
    /**
     * Validates whether the given integer number is positive. If the number is not positive (i.e., less than zero),
     * an IllegalArgumentException is thrown with a descriptive error message indicating the field name and the requirement
     * for the number to be positive.
     *
     * @param number     The integer number to be validated for positivity.
     * @param fieldName  The name of the field or variable corresponding to the given number. This is used in the error
     *                   message to identify the field causing the validation failure.
     * @throws IllegalArgumentException  If the given number is not positive (less than zero).
     */
    protected void shouldBePositive(int number, String fieldName) {
        if (number < 0) {
            throw new IllegalArgumentException("[" + fieldName + "] should be positive.");
        }
    }

    /**
     * Validates whether the given integer values are in ascending order. If the 'min' value is greater than the 'max' value,
     * an IllegalArgumentException is thrown with a descriptive error message indicating the relationship between the two
     * fields and the requirement for 'min' to be less than or equal to 'max'.
     *
     * @param min       The minimum integer value to be validated.
     * @param max       The maximum integer value to be validated.
     * @param field1    The name of the field or variable corresponding to the 'min' value. This is used in the error
     *                  message to identify the first field involved in the validation failure.
     * @param field2    The name of the field or variable corresponding to the 'max' value. This is used in the error
     *                  message to identify the second field involved in the validation failure.
     * @throws IllegalArgumentException  If the 'min' value is greater than the 'max' value, indicating that they are not
     *                                   in ascending order.
     */
    protected void shouldBeInAscOrder(int min, int max, String field1, String field2) {
        if (min > max) {
            throw new IllegalArgumentException("[" + field1 + "] should be less than [" + field2 + "].");
        }
    }

    /**
     * Validates whether the given floating-point values are in ascending order. If the 'min' value is greater than the 'max' value,
     * an IllegalArgumentException is thrown with a descriptive error message indicating the relationship between the two
     * fields and the requirement for 'min' to be less than or equal to 'max'.
     *
     * @param min       The minimum floating-point value to be validated.
     * @param max       The maximum floating-point value to be validated.
     * @param field1    The name of the field or variable corresponding to the 'min' value. This is used in the error
     *                  message to identify the first field involved in the validation failure.
     * @param field2    The name of the field or variable corresponding to the 'max' value. This is used in the error
     *                  message to identify the second field involved in the validation failure.
     * @throws IllegalArgumentException  If the 'min' value is greater than the 'max' value, indicating that they are not
     *                                   in ascending order.
     */
    protected void shouldBeInAscOrder(float min, float max, String field1, String field2) {
        if (min > max) {
            throw new IllegalArgumentException("[" + field1 + "] should be less than [" + field2 + "].");
        }
    }
}
