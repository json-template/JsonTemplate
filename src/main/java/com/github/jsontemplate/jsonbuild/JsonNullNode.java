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

/**
 * This class represents a producer of a json null value.
 */
public final class JsonNullNode implements JsonValueNode {

    @Override
    public String compactString() {
        return "null";
    }

    @Override
    public String prettyString(int identation) {
        return compactString();
    }
}
