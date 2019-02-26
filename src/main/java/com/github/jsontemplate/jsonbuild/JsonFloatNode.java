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

package com.github.jsontemplate.jsonbuild;

import java.util.function.Supplier;

/**
 * This class represents a producer of a json float value.
 */
public final class JsonFloatNode extends AbstractJsonValueNode<Float> {

    public JsonFloatNode(Supplier<Float> supplier) {
        super(supplier);
    }

    /**
     * Creates a JsonFloatNode with a given float value.
     *
     * @param value
     * @return
     */
    public static JsonFloatNode of(Float value) {
        return new JsonFloatNode(() -> value);
    }

    @Override
    public String compactString() {
        return Float.toString(supplier.get());
    }
}
