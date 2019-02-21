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

import java.util.Collection;
import java.util.Map;

/**
 * Interface for all type of json nodes.
 */
public interface JsonNode {

    /**
     * Creates a JsonNode based on a given object.
     * It support the following types:
     * <ul>
     * <li>null - converted to JsonNullNode</li>
     * <li>Integer - converted to JsonIntegerNode</li>
     * <li>Float - converted to JsonFloatNode</li>
     * <li>Boolean - converted to JsonBooleanNode</li>
     * <li>String - converted to JsonStringNode</li>
     * <li>Array - converted to JsonArrayNode</li>
     * <li>Collection - converted to JsonArrayNode</li>
     * <li>Map - converted to JsonArrayNode</li>
     * <li>otherwise, it is conveted to JsonStringNode with
     * the string representation of the object.
     * </li>
     * </ul>
     *
     * @param obj
     * @return
     */
    static JsonNode of(Object obj) {
        JsonNode jsonNode;
        if (obj == null) {
            jsonNode = new JsonNullNode();
        } else if (obj instanceof Integer) {
            jsonNode = JsonIntegerNode.of((Integer) obj);
        } else if (obj instanceof Float) {
            jsonNode = JsonFloatNode.of((Float) obj);
        } else if (obj instanceof Boolean) {
            jsonNode = JsonBooleanNode.of((Boolean) obj);
        } else if (obj instanceof String) {
            jsonNode = JsonStringNode.of((String) obj);
        } else if (obj.getClass().isArray()) {
            jsonNode = JsonArrayNode.of((Object[]) obj);
        } else if (obj instanceof Collection) {
            jsonNode = JsonArrayNode.of((Collection<?>) obj);
        } else if (obj instanceof Map) {
            jsonNode = JsonObjectNode.of((Map) obj);
        } else {
            jsonNode = JsonStringNode.of(obj.toString());
        }
        return jsonNode;
    }

    /**
     * Produces a json string in a compact format.
     *
     * @return
     */
    String print();

    /**
     * Produces a json string with identations.
     *
     * @param identation
     * @return
     */
    String prettyPrint(int identation);
}
