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

/**
 * This class is a wrapper of another JsonNode. It is used
 * as a placeholder of an unknown or future-created JsonNode.
 */
public final class JsonWrapperNode implements JsonNode {

    private JsonNode jsonNode;

    public JsonNode getJsonNode() {
        return jsonNode;
    }

    public void setJsonNode(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    @Override
    public String compactString() {
        return jsonNode.compactString();
    }

    @Override
    public String prettyString(int indentation) {
        return jsonNode.prettyString(indentation);
    }
}
