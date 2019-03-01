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

package com.github.jsontemplate.modelbuild;

import com.github.jsontemplate.jsonbuild.JsonBuilder;
import com.github.jsontemplate.jsonbuild.JsonNode;
import com.github.jsontemplate.modelbuild.handler.DefaultBuildHandler;
import com.github.jsontemplate.valueproducer.IValueProducer;

import java.util.Map;

final class ArrayPropertyDeclaration extends BasePropertyDeclaration {

    ArrayPropertyDeclaration(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public void buildJsonTemplate(JsonBuilder builder, Map<String, IValueProducer> producerMap,
                                  Map<String, JsonNode> typeMap,
                                  Map<String, JsonNode> variableMap, String defaultTypeName,
                                  DefaultBuildHandler defaultHandler) {
        if (parent == null) {
            builder.createArray();
        } else {
            if (builder.inObject()) {
                builder.putArray(propertyName);
            } else if (builder.inArray()) {
                builder.addArray();
            }
        }
        buildChildrenJsonTemplate(builder, producerMap, typeMap, variableMap, defaultTypeName, defaultHandler);
        if (this.typeSpec.getTypeName() == null) {
            TypeSpec ancestorTypeSpec = findAncestorTypeSpec(defaultTypeName);
            this.typeSpec.setTypeName(ancestorTypeSpec.getTypeName());
        }

        JsonNode jsonNode = buildNodeFromProducer(producerMap);

        if (jsonNode == null) {
            jsonNode = typeMap.get(this.typeSpec.getTypeName());
        }
        setArrayInfo(builder.peekArrayNode(), jsonNode);
        builder.end();
    }

    @Override
    public void applyVariablesToParameters(Map<String, Object> variableMap) {
        this.properties.forEach(p -> p.applyVariablesToParameters(variableMap));
    }
}
