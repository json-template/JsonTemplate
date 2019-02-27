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

import com.github.jsontemplate.jsonbuild.JsonRawNode;

/**
 * This class produces a {@link JsonRawNode} which prints the content
 * of the given string without quotes. This is suitable for embedding
 * another json string.
 */
public class RawStringNodeProducer extends AbstractNodeProducer<JsonRawNode> {

    /**
     * The type name used in the template, e.g. {aStringField: @raw}
     */
    public static final String TYPE_NAME = "raw";

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public JsonRawNode produce(String value) {
        return new JsonRawNode(() -> value);
    }

}
