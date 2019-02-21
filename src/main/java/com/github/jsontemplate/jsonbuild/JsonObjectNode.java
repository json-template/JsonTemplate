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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represents a producer of a json object value.
 */
public final class JsonObjectNode implements JsonNode {

    private Map<String, JsonNode> children = new LinkedHashMap<>();

    /**
     * Creates a JsonObjectNode with a given map.
     *
     * @param valueMap
     * @return
     */
    public static JsonObjectNode of(Map<String, ?> valueMap) {
        JsonObjectNode jsonObjectNode = new JsonObjectNode();
        valueMap.forEach((key, value) -> jsonObjectNode.putNode(key, JsonNode.of(value)));
        return jsonObjectNode;
    }

    /**
     * Adds a child
     *
     * @param key
     * @param node
     */
    public void putNode(String key, JsonNode node) {
        this.children.put(key, node);
    }

    @Override
    public String print() {
        String joinedChildren = children.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue().print())
                .collect(Collectors.joining(","));
        return "{" + joinedChildren + "}";
    }

    @Override
    public String prettyPrint(int identation) {
        String childSpaces = JsonNodeUtils.makeIdentation(identation + 1);
        String joinedIdentChildren = children.entrySet().stream()
                .map(entry -> childSpaces + "\"" + entry.getKey() + "\" : " + entry.getValue().prettyPrint(identation + 1))
                .collect(Collectors.joining(",\n"));
        String spaces = JsonNodeUtils.makeIdentation(identation);
        return "{\n" +
                joinedIdentChildren +
                "\n" + spaces + "}";
    }

}
