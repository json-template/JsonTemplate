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

public class SmartNodeProducer extends AbstractNodeProducer<JsonNode> {

    public static final String TYPE_NAME = "smart";

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
        if ("null".equalsIgnoreCase(value)) {
            return new JsonNullNode();
        }
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
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

    protected boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    protected boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
