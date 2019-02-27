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

import com.github.jsontemplate.jsonbuild.JsonBooleanNode;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class produces a {@link JsonBooleanNode JsonBooleanNode} which can generate json boolean value.
 */
public class BooleanNodeProducer extends AbstractNodeProducer<JsonBooleanNode> {

    /**
     * The type name used in the template, e.g. {aBooleanField: @b}
     */
    public static final String TYPE_NAME = "b";

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate a random boolean value
     *
     * @return
     */
    @Override
    public JsonBooleanNode produce() {
        return new JsonBooleanNode(() -> new Random().nextBoolean());
    }

    /**
     * Produces a node which can generate a fixed boolean value
     *
     * @param value
     * @return
     */
    @Override
    public JsonBooleanNode produce(String value) {
        boolean parsedBoolean = Boolean.parseBoolean(value);
        return new JsonBooleanNode(() -> parsedBoolean);
    }

    /**
     * Produces a node which selects a string in a list.
     * The selected string is parsed to a boolean.
     *
     * @param valueList the enumerated string values
     * @return
     */
    @Override
    public JsonBooleanNode produce(List<String> valueList) {
        List<Boolean> parsedValueList = valueList.stream().map(Boolean::parseBoolean).collect(Collectors.toList());
        return new JsonBooleanNode(() -> parsedValueList.get(new Random().nextInt(parsedValueList.size())));
    }
}
