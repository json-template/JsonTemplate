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

import com.github.jsontemplate.jsonbuild.*;
import com.github.jsontemplate.modelbuild.handler.DefaultBuildHandler;
import com.github.jsontemplate.valueproducer.IValueProducer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The base class for all property declarations.
 */
public class BasePropertyDeclaration {

    protected String propertyName;
    protected TypeSpec typeSpec = new TypeSpec();
    protected List<BasePropertyDeclaration> properties = new ArrayList<>();
    protected BasePropertyDeclaration parent;
    protected TypeSpec arrayTypeSpec = new TypeSpec();
    protected boolean isTypeDefinition = false;

    public TypeSpec getTypeSpec() {
        return typeSpec;
    }

    public TypeSpec getArrayTypeSpec() {
        return arrayTypeSpec;
    }

    public List<BasePropertyDeclaration> getProperties() {
        return properties;
    }

    public void markAsTypeDefinition() {
        isTypeDefinition = true;
    }

    public boolean isTypeDefinition() {
        return isTypeDefinition;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }


    public void addProperty(BasePropertyDeclaration propertyDeclaration) {
        this.properties.add(propertyDeclaration);
        propertyDeclaration.setParent(this);
    }

    public void removeProperty(BasePropertyDeclaration propertyDeclaration) {
        this.properties.remove(propertyDeclaration);
        propertyDeclaration.setParent(null);
    }

    public BasePropertyDeclaration getParent() {
        return parent;
    }

    public void setParent(BasePropertyDeclaration parent) {
        this.parent = parent;
    }

    ArrayPropertyDeclaration asArrayProperty() {
        return new ArrayPropertyDeclaration(this.propertyName);
    }

    ObjectPropertyDeclaration asObjectProperty() {
        return new ObjectPropertyDeclaration(this.propertyName);
    }

    /**
     * Builds a JSON template using the provided parameters and adds the resulting JSON to the given JsonBuilder.
     *
     * @param builder       The JsonBuilder to which the JSON template is added.
     * @param producerMap   A map of String keys and IValueProducer values. The keys represent variable names, and the
     *                      values are responsible for producing values for those variables during JSON template building.
     * @param typeMap       A map of String keys and JsonNode values. The keys represent type names, and the values are
     *                      JsonNodes representing the corresponding JSON types.
     * @param variableMap   A map of String keys and JsonNode values. The keys represent variable names, and the values are
     *                      JsonNodes representing the values for those variables.
     * @param defaultTypeName   The default type name to be used in case the type name is not specified in the TypeSpec.
     * @param defaultHandler    The DefaultBuildHandler used to handle cases where no matching type is found during the
     *                          JSON template building process.
     */
    public void buildJsonTemplate(JsonBuilder builder,
                                  Map<String, IValueProducer<? extends JsonNode>> producerMap,
                                  Map<String, JsonNode> typeMap,
                                  Map<String, JsonNode> variableMap,
                                  String defaultTypeName,
                                  DefaultBuildHandler defaultHandler) {
        JsonNode jsonNode = null;
        if (isNullValue()) {
            jsonNode = new JsonNullNode();
        } else {
            jsonNode = findJsonNodeFromVariable(variableMap, typeSpec.getTypeName());
            if (jsonNode == null) {
                // it is not a variable, search type map
                if (typeSpec.getTypeName() == null) {
                    TypeSpec ancestorTypeSpec = findAncestorTypeSpec(defaultTypeName);
                    this.typeSpec.setTypeName(ancestorTypeSpec.getTypeName());
                    if (typeSpec.getSingleParam() == null) {
                        this.typeSpec = ancestorTypeSpec;
                    }
                }

                jsonNode = buildNodeFromProducer(producerMap);
            }
            if (jsonNode == null) {
                // this type is declared inside template
                jsonNode = typeMap.get(this.typeSpec.getTypeName());
            }
            if (jsonNode == null) {
                // cannot find any matched type
                defaultHandler.handle(this.typeSpec.getTypeName());
            }
        }

        joinNode(builder, jsonNode);
    }

    private void joinNode(JsonBuilder builder, JsonNode jsonNode) {
        if (builder.inObject()) {
            builder.putNode(propertyName, jsonNode);
        } else if (builder.inArray()) {
            builder.addNode(jsonNode);
        } else {
            builder.pushNode(jsonNode);
        }
    }

    protected TypeSpec findAncestorTypeSpec(String defaultTypeName) {
        TypeSpec curTypeSpec = this.typeSpec;
        BasePropertyDeclaration declParent = this.getParent();
        while (curTypeSpec.getTypeName() == null && declParent != null) {
            curTypeSpec = declParent.getTypeSpec();
            declParent = declParent.getParent();
        }
        if (curTypeSpec.getTypeName() == null) {
            curTypeSpec = getDefaultTypeSpec(defaultTypeName);
        }
        return curTypeSpec;
    }

    /**
     * Finds the ancestor TypeSpec for the current BasePropertyDeclaration instance. An ancestor TypeSpec is the closest
     * parent TypeSpec in the inheritance hierarchy whose type name is not null.
     *
     * @param defaultTypeName   The default type name to be used in case no ancestor TypeSpec with a non-null type name
     *                          is found in the inheritance hierarchy.
     * @return The TypeSpec representing the ancestor type, or a default TypeSpec if no non-null ancestor type is found.
     */
    protected TypeSpec getDefaultTypeSpec(String defaultTypeName) {
        TypeSpec defaultTypeSpec = new TypeSpec();
        defaultTypeSpec.setTypeName(defaultTypeName);
        return defaultTypeSpec;
    }

    /**
     * Builds a JsonNode from the given IValueProducer.
     * @param producerMap  A map of String keys and IValueProducer values. The keys represent variable names, and the
     * @return The JsonNode produced by the IValueProducer.
     */
    protected JsonNode buildNodeFromProducer(Map<String, IValueProducer<? extends JsonNode>> producerMap) {
        JsonNode jsonNode = null;
        IValueProducer producer = producerMap.get(this.typeSpec.getTypeName());
        if (producer != null) {
            if (typeSpec.getSingleParam() != null) {
                jsonNode = producer.produce(typeSpec.getSingleParam());
            } else if (!typeSpec.getListParam().isEmpty()) {
                jsonNode = producer.produce(typeSpec.getListParam());
            } else if (!typeSpec.getMapParam().isEmpty()) {
                jsonNode = producer.produce(typeSpec.getMapParam());
            } else {
                jsonNode = producer.produce();
            }
        }
        return jsonNode;
    }

    /**
     * Handles the case where the current BasePropertyDeclaration instance is a composite property (i.e. an array or
     * object property).
     *
     * @param builder      The JsonBuilder to which the JSON template is added.
     * @param producerMap  A map of String keys and IValueProducer values. The keys represent variable names, and the
     * @param typeMap       A map of String keys and JsonNode values. The keys represent type names, and the values are
     * @param missTypeMap  A map of String keys and List values. The keys represent type names, and the values are
     * @param variableMap  A map of String keys and JsonNode values. The keys represent variable names, and the values are
     */
    protected void handleComposite(JsonBuilder builder, Map<String, IValueProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, List<JsonWrapperNode>> missTypeMap, Map<String, JsonNode> variableMap) {
        throw new UnsupportedOperationException("Unexpected operation in simple property.");
    }

    protected void setArrayInfo(JsonArrayNode jsonArrayNode, JsonNode defaultNode) {
        jsonArrayNode.setDefaultNode(defaultNode);
        if (this.arrayTypeSpec.getSingleParam() != null) {
            jsonArrayNode.setParameters(this.arrayTypeSpec.getSingleParam());
        }
        if (!this.arrayTypeSpec.getListParam().isEmpty()) {
            jsonArrayNode.setParameters(this.arrayTypeSpec.getListParam());
        }
        if (!this.arrayTypeSpec.getMapParam().isEmpty()) {
            jsonArrayNode.setParameters(this.arrayTypeSpec.getMapParam());
        }
    }

    protected void buildChildrenJsonTemplate(JsonBuilder builder, Map<String, IValueProducer<? extends JsonNode>> producerMap,
                                             Map<String, JsonNode> typeMap,
                                             Map<String, JsonNode> variableMap, String defaultTypeName,
                                             DefaultBuildHandler defaultHandler) {
        for (BasePropertyDeclaration declaration : properties) {
            declaration.buildJsonTemplate(builder, producerMap, typeMap, variableMap, defaultTypeName, defaultHandler);
        }
    }

    boolean isTypeDeclaration() {
        return propertyName != null && propertyName.startsWith(Token.TYPE.getTag());
    }

    private JsonNode findJsonNodeFromVariable(Map<String, JsonNode> variableMap, String name) {
        if (name != null && name.startsWith(Token.VARIABLE.getTag())) {
            return variableMap.get(name.substring(1));
        }
        return null;
    }

    public void applyVariablesToParameters(Map<String, Object> variableMap) {
        if (typeSpec.getSingleParam() != null) {
            applyVariablesToSingleParameter(variableMap);
        } else if (!typeSpec.getListParam().isEmpty()) {
            applyVariablesToListParameter(variableMap);
        } else if (!typeSpec.getMapParam().isEmpty()) {
            applyVariablesToMapParameter(variableMap);
        }
    }

    private void applyVariablesToSingleParameter(Map<String, Object> variableMap) {
        if (typeSpec.getSingleParam().startsWith(Token.VARIABLE.getTag())) {
            Object variable = variableMap.get(typeSpec.getSingleParam().substring(1));
            if (variable instanceof Collection<?>) {
                Collection<?> collectionVariable = (Collection<?>) variable;
                typeSpec.setSingleParam(null);
                List<String> elements = collectionVariable.stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                typeSpec.setListParam(elements);

            } else if (variable.getClass().isArray()) {
                Object[] arrayVariable = (Object[]) variable;
                typeSpec.setSingleParam(null);
                List<String> elements = Arrays.stream(arrayVariable)
                        .map(Object::toString)
                        .collect(Collectors.toList());
                typeSpec.setListParam(elements);

            } else if (variable instanceof Map) {
                typeSpec.setSingleParam(null);
                typeSpec.getListParam().clear();
                Map<String, Object> mapVariable = (Map<String, Object>) variable;
                Map<String, String> config = mapVariable.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
                typeSpec.setMapParam(config);

            } else {
                typeSpec.setSingleParam(variable.toString());
            }
        }
    }

    private void applyVariablesToListParameter(Map<String, Object> variableMap) {
        for (int i = 0; i < typeSpec.getListParam().size(); i++) {
            if (typeSpec.getListParam().get(i).startsWith(Token.VARIABLE.getTag())) {
                Object variable = variableMap.get(typeSpec.getListParam().get(i).substring(1));
                typeSpec.getListParam().set(i, variable.toString());
            }
        }
    }

    private void applyVariablesToMapParameter(Map<String, Object> variableMap) {
        for (Map.Entry<String, String> entry : typeSpec.getMapParam().entrySet()) {
            if (entry.getValue().startsWith(Token.VARIABLE.getTag())) {
                Object variable = variableMap.get(entry.getValue().substring(1));
                if (variable != null) {
                    typeSpec.getMapParam().put(entry.getKey(), variable.toString());
                } else {
                    throw new IllegalArgumentException("Unknown variable name: " + entry.getValue());
                }
            }
        }
    }

    public boolean isNullValue() {
        return "null".equals(typeSpec.getSingleParam());
    }

    protected boolean isRoot() {
        return parent == null;
    }
}
