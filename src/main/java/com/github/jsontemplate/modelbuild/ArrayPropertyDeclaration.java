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

    /**
     * Builds a JSON template using the provided parameters and adds the resulting JSON elements to the given JsonBuilder.
     * The method handles the creation of JSON arrays or nested arrays based on the parent-child relationship.
     *
     * @param builder         The JsonBuilder to which the JSON elements are added during the template building process.
     * @param producerMap     A map of String keys and IValueProducer values. The keys represent variable names, and the
     *                        values are responsible for producing values for those variables during JSON template building.
     * @param typeMap         A map of String keys and JsonNode values. The keys represent type names, and the values are
     *                        JsonNodes representing the corresponding JSON types.
     * @param variableMap     A map of String keys and JsonNode values. The keys represent variable names, and the values are
     *                        JsonNodes representing the values for those variables.
     * @param defaultTypeName The default type name to be used in case the type name is not specified in the TypeSpec.
     * @param defaultHandler  The DefaultBuildHandler used to handle cases where no matching type is found during the
     *                        JSON template building process.
     */
    @Override
    public void buildJsonTemplate(JsonBuilder builder, Map<String, IValueProducer<? extends JsonNode>> producerMap,
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
        if (jsonNode == null) {
            jsonNode = defaultHandler.handle(this.typeSpec.getTypeName());
        }
        setArrayInfo(builder.peekArrayNode(), jsonNode);
        builder.end();
    }



    @Override
    public void applyVariablesToParameters(Map<String, Object> variableMap) {
        this.properties.forEach(p -> p.applyVariablesToParameters(variableMap));
    }
}
