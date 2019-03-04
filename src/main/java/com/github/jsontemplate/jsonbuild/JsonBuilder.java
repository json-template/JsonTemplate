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

import java.util.Deque;
import java.util.LinkedList;

/**
 * The builder of the data structure consisting of json nodes.
 */
public final class JsonBuilder {

    private Deque<JsonNode> nodeStack = new LinkedList<>();
    private JsonNode lastPopNode;

    public JsonBuilder createArray() {
        nodeStack.push(new JsonArrayNode());
        return this;
    }

    public JsonBuilder createObject() {
        nodeStack.push(new JsonObjectNode());
        return this;
    }

    public JsonBuilder end() {
        lastPopNode = nodeStack.pop();
        return this;
    }

    public JsonBuilder putArray(String key) {
        JsonArrayNode jsonArrayNode = new JsonArrayNode();
        ((JsonObjectNode) nodeStack.peek()).putNode(key, jsonArrayNode);
        nodeStack.push(jsonArrayNode);
        return this;
    }

    public JsonBuilder putObject(String key) {
        JsonObjectNode jsonObjectNode = new JsonObjectNode();
        ((JsonObjectNode) nodeStack.peek()).putNode(key, jsonObjectNode);
        nodeStack.push(jsonObjectNode);
        return this;
    }

    public JsonBuilder putNode(String key, JsonNode node) {
        ((JsonObjectNode) nodeStack.peek()).putNode(key, node);
        return this;
    }

    public JsonBuilder addNode(JsonNode node) {
        ((JsonArrayNode) nodeStack.peek()).addNode(node);
        return this;
    }

    public JsonBuilder pushNode(JsonNode node) {
        nodeStack.push(node);
        return this;
    }

    public JsonBuilder addArray() {
        JsonArrayNode jsonArrayNode = new JsonArrayNode();
        ((JsonArrayNode) nodeStack.peek()).addNode(jsonArrayNode);
        nodeStack.push(jsonArrayNode);
        return this;
    }

    public JsonBuilder addObject() {
        JsonObjectNode jsonObjectNode = new JsonObjectNode();
        ((JsonArrayNode) nodeStack.peek()).addNode(jsonObjectNode);
        nodeStack.push(jsonObjectNode);
        return this;
    }

    public boolean inObject() {
        return !nodeStack.isEmpty() && nodeStack.peek() instanceof JsonObjectNode;
    }

    public boolean inArray() {
        return !nodeStack.isEmpty() && nodeStack.peek() instanceof JsonArrayNode;
    }

    public JsonArrayNode peekArrayNode() {
        return ((JsonArrayNode) nodeStack.peek());
    }

    public JsonNode build() {
        if (!nodeStack.isEmpty()) {
            return nodeStack.peek();
        } else {
            return lastPopNode;
        }
    }
}
