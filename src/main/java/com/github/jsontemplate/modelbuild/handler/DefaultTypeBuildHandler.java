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

package com.github.jsontemplate.modelbuild.handler;

import com.github.jsontemplate.jsonbuild.JsonNode;
import com.github.jsontemplate.jsonbuild.JsonWrapperNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DefaultTypeBuildHandler implements DefaultBuildHandler {

    private Map<String, List<JsonWrapperNode>> missTypeMap;

    public DefaultTypeBuildHandler(Map<String, List<JsonWrapperNode>> missTypeMap) {
        this.missTypeMap = missTypeMap;
    }

    @Override
    public JsonNode handle(String valueTypeName) {
        JsonWrapperNode jsonNode = new JsonWrapperNode();
        List<JsonWrapperNode> jsonWrapperNodes = missTypeMap.get(valueTypeName);
        if (jsonWrapperNodes == null) {
            List<JsonWrapperNode> wrapperNodes = new ArrayList<>();
            wrapperNodes.add(jsonNode);
            missTypeMap.put(valueTypeName, wrapperNodes);
        } else {
            jsonWrapperNodes.add(jsonNode);
        }
        return jsonNode;
    }

}
