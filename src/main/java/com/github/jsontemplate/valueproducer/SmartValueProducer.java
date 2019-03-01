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

import com.github.jsontemplate.jsonbuild.*;

/**
 * This is the default type in JsonTemplate. It produces a value with the type which
 * the value looks like. For example:
 *
 * <ul>
 * <li>"null" results in <i>null</i></li>
 * <li>"true" results in <i>true</i></li>
 * <li>"fAlSe" results in <i>false</i></li>
 * <li>"1" results in <i>1</i></li>
 * <li>"1f" results in <i>1.0</i></li>
 * <li>"hello" results in <i>"hello"</i></li>
 * </ul>
 * <p>
 * The smart type is designed to be used only for the fixed value.
 * For example:
 * <br/>
 * {name : @smart(John)} will result in {name : John}
 * <br/>
 * However,
 * <br/>
 * {name : @smart} will result in {name : null}
 */
public class SmartValueProducer extends AbstractValueProducer<JsonNode> {

    /**
     * The type name used in the template, e.g. @smart{ name: John}
     */
    public static final String TYPE_NAME = "smart";
    private static final String NULL = "null";

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public JsonNode produce() {
        return new JsonNullNode();
    }

    @Override
    public JsonNode produce(String value) {
        if (isNull(value)) {
            return new JsonNullNode();
        }
        if (isBoolean(value)) {
            return new JsonBooleanNode(() -> Boolean.parseBoolean(value));
        }
        if (isInteger(value)) {
            return new JsonIntegerNode(() -> Integer.parseInt(value));
        }
        if (isFloat(value)) {
            return new JsonFloatNode(() -> Float.parseFloat(value));
        }
        return new JsonStringNode(() -> value);
    }

    /**
     * Checks if the value represents null.
     *
     * @param value
     * @return
     */
    protected boolean isNull(String value) {
        return NULL.equalsIgnoreCase(value);
    }

    /**
     * Checks if the value represents a boolean value.
     *
     * @param value
     * @return
     */
    protected boolean isBoolean(String value) {
        return Boolean.TRUE.toString().equalsIgnoreCase(value) ||
                Boolean.FALSE.toString().equalsIgnoreCase(value);
    }

    /**
     * Checks if the value represents an integer value.
     *
     * @param value
     * @return
     */
    protected boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Checks if the value represents a float value.
     *
     * @param value
     * @return
     */
    protected boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
