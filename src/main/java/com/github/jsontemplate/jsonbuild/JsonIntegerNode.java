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

package com.github.jsontemplate.jsonbuild;

import java.util.function.Supplier;

public final class JsonIntegerNode extends AbstractJsonValueNode<Integer> {

    public JsonIntegerNode(Supplier<Integer> supplier) {
        super(supplier);
    }

    /**
     * Creates a JsonIntegerNode with a given integer value.
     *
     * @param value the integer value to be converted
     * @return the converted json integer node
     */
    public static JsonIntegerNode of(Integer value) {
        return new JsonIntegerNode(() -> value);
    }

    @Override
    public String compactString() {
        return Integer.toString(supplier.get());
    }
}
