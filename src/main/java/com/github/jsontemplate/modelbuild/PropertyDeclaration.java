package com.github.jsontemplate.modelbuild;

import com.github.jsontemplate.jsonbuild.JsonArrayNode;
import com.github.jsontemplate.jsonbuild.JsonBuilder;
import com.github.jsontemplate.jsonbuild.JsonNode;
import com.github.jsontemplate.jsonbuild.JsonNullNode;
import com.github.jsontemplate.jsonbuild.JsonWrapperNode;
import com.github.jsontemplate.valueproducer.INodeProducer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PropertyDeclaration {
    protected String valueName;

    public TypeSpec getTypeSpec() {
        return typeSpec;
    }

    public void setTypeSpec(TypeSpec typeSpec) {
        this.typeSpec = typeSpec;
    }

    public TypeSpec getArrayTypeSpec() {
        return arrayTypeSpec;
    }

    public void setArrayTypeSpec(TypeSpec arrayTypeSpec) {
        this.arrayTypeSpec = arrayTypeSpec;
    }

    protected TypeSpec typeSpec;
    protected boolean isObject;
    protected boolean isArray;

    protected List<PropertyDeclaration> properties = new ArrayList<>();
    protected PropertyDeclaration parent;

    protected TypeSpec arrayTypeSpec;

    public List<PropertyDeclaration> getProperties() {
        return properties;
    }


    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }


    public void addProperty(PropertyDeclaration propertyDeclaration) {
        this.properties.add(propertyDeclaration);
        propertyDeclaration.setParent(this);
    }

    public void removeProperty(PropertyDeclaration propertyDeclaration) {
        this.properties.remove(propertyDeclaration);
        propertyDeclaration.setParent(null);
    }

    public PropertyDeclaration getParent() {
        return parent;
    }

    public void setParent(PropertyDeclaration parent) {
        this.parent = parent;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setAsArray(boolean array) {
        isArray = array;
    }

    public boolean isObject() {
        return isObject;
    }

    public void setAsObject(boolean object) {
        isObject = object;
    }

    public void collectTypeDeclaration(List<PropertyDeclaration> typeList) {
        if (isTypeDeclaration()) {
            typeList.add(this);
        }
        for (PropertyDeclaration declaration : properties) {
            declaration.collectTypeDeclaration(typeList);
        }
    }

    private void buildJsonTemplate(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, List<JsonWrapperNode>> missTypeMap, Map<String, JsonNode> variableMap) {
        if (!isObject && !isArray) { // plain value
            handlePlainValue(builder, producerMap, typeMap, variableMap, new DefaultHandlerForBuildType(missTypeMap));
        } else {
            handleComposite(builder, producerMap, typeMap, missTypeMap, variableMap);
        }
    }

    private void handlePlainValue(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, JsonNode> variableMap, DefaultHandler defaultHandler) {
        JsonNode jsonNode = null;
        if (isNullValue()) {
            jsonNode = new JsonNullNode();
        } else {
            jsonNode = findJsonNodeFromVariable(variableMap, typeSpec.getTypeName());
            if (jsonNode == null) { // it is not a variable, search type map
                if (typeSpec.getTypeName() == null && typeSpec.getSingleParam() == null) {
                    findDefaultJsonNode();

                    if (jsonNode == null) { // cannot find any matched type
                        defaultHandler.handle(valueTypeName);
                    }
                }

                String valueTypeName = findValueType();
                jsonNode = buildNodeFromProducer(producerMap, valueTypeName);

                if (jsonNode == null) { // this type is declared inside template
                    jsonNode = typeMap.get(valueTypeName);
                }
                if (jsonNode == null) { // cannot find any matched type
                    defaultHandler.handle(valueTypeName);
                }

            }
        }

        putOrAddNode(builder, jsonNode);
    }

    private JsonNode findValueTypeAndBuildNode(Map<String, INodeProducer> producerMap) {

    }

    public void buildType(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, List<JsonWrapperNode>> missTypeMap, Map<String, JsonNode> variableMap) {
        buildJsonTemplate(builder, producerMap, typeMap, missTypeMap, variableMap);
    }

    public void buildJson(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, List<JsonWrapperNode>> missTypeMap, Map<String, JsonNode> variableMap) {
        // TODO: build defaultTypeNode for every type
        buildJsonTemplate(builder, producerMap, typeMap, missTypeMap, variableMap);
    }

    private void putOrAddNode(JsonBuilder builder, JsonNode jsonNode) {
        if (builder.inObject()) {
            builder.putNode(valueName, jsonNode);
        } else if (builder.inArray()) {
            builder.addNode(jsonNode);
        }
    }

    private String findValueType() {
        String valueTypeName = typeSpec.getTypeName();
        PropertyDeclaration declParent = this.getParent();
        while (valueTypeName == null && declParent != null) {
            valueTypeName = declParent.getTypeSpec().getTypeName();
            declParent = declParent.getParent();
        }
        if (valueTypeName == null) {
            valueTypeName = "s"; // todo improve, temporary solution for array default type
        }
        return valueTypeName;
    }

    private JsonNode buildNodeFromProducer(Map<String, INodeProducer> producerMap, String valueTypeName) {
        JsonNode jsonNode = null;
        INodeProducer producer = producerMap.get(valueTypeName);
        if (producer != null) {
            if (typeSpec.getSingleParam() != null) {
                jsonNode = producer.produce(typeSpec.getSingleParam());
            } else if (typeSpec.getListParam() != null) {
                jsonNode = producer.produce(typeSpec.getListParam());
            } else if (typeSpec.getMapParam() != null) {
                jsonNode = producer.produce(typeSpec.getMapParam());
            } else {
                jsonNode = producer.produce();
            }
        }
        return jsonNode;
    }

    private void handleComposite(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, List<JsonWrapperNode>> missTypeMap, Map<String, JsonNode> variableMap) {
        if (parent == null) {
            if (isObject) {
                builder.createObject();
            } else if (isArray) {
                builder.createArray();
            }
        } else {
            if (isObject) {
                if (builder.inObject()) {
                    builder.putObject(valueName);
                } else if (builder.inArray()) {
                    builder.addObject();
                }

            } else if (isArray) {
                if (builder.inObject()) {
                    builder.putArray(valueName);
                } else if (builder.inArray()) {
                    builder.addArray();
                }
            }
        }
        buildChildrenJson(builder, producerMap, typeMap, missTypeMap, variableMap);
        if (isArray) {
            String valueTypeName = findValueType();
            JsonNode jsonNode = buildNodeFromProducer(producerMap, valueTypeName);

            if (jsonNode == null) {
                jsonNode = typeMap.get(valueTypeName);
            }
            setArrayInfo(builder.peekArrayNode(), jsonNode);

        }
        builder.end();
    }

    private void setArrayInfo(JsonArrayNode jsonArrayNode, JsonNode defaultNode) {
        jsonArrayNode.setDefaultNode(defaultNode);
        if (this.arrayTypeSpec.getSingleParam() != null) {
            jsonArrayNode.setParameters(this.arrayTypeSpec.getSingleParam());
        }
        if (this.arrayTypeSpec.getListParam() != null) {
            jsonArrayNode.setParameters(this.arrayTypeSpec.getListParam());
        }
        if (this.arrayTypeSpec.getMapParam() != null) {
            jsonArrayNode.setParameters(this.arrayTypeSpec.getMapParam());
        }
    }

    private void buildChildrenJson(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, List<JsonWrapperNode>> missTypeMap, Map<String, JsonNode> variableMap) {
        for (PropertyDeclaration declaration : properties) {
            declaration.buildJson(builder, producerMap, typeMap, missTypeMap, variableMap);
        }
    }

    boolean isTypeDeclaration() {
        return valueName != null && valueName.startsWith("@");
    }

    private JsonNode findJsonNodeFromVariable(Map<String, JsonNode> variableMap, String name) {
        if (name != null && name.startsWith("$")) {
            return variableMap.get(name.substring(1));
        }
        return null;
    }


    public void applyVariables(Map<String, Object> variableMap) {
        if (isArray || isObject){
            this.properties.forEach(p -> p.applyVariables(variableMap));
        } else {
            if (typeSpec.getSingleParam() != null) {
                if (typeSpec.getSingleParam().startsWith("$")) {
                    Object variable = variableMap.get(typeSpec.getSingleParam().substring(1));
                    if (variable instanceof Collection<?>) {
                        Collection<?> collectionVariable = (Collection<?>) variable;
                        typeSpec.setSingleParam(null);
                        List<String> elements = collectionVariable.stream()
                                .map(Object::toString)
                                .collect(Collectors.toList());
                        typeSpec.setListParam(elements);
                        // todo primitives
                    } else if (variable.getClass().isArray()) {
                        Object[] arrayVariable = (Object[]) variable;
                        typeSpec.setSingleParam(null);
                        List<String> elements = Arrays.stream(arrayVariable)
                                .map(Object::toString)
                                .collect(Collectors.toList());
                        typeSpec.setListParam(elements);
                        // todo primitives
                    } else if (variable instanceof Map) {
                        typeSpec.setSingleParam(null);
                        typeSpec.setListParam(null);
                        Map<String, Object> mapVariable = (Map<String, Object>) variable;
                        Map<String, String> config = mapVariable.entrySet().stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
                        typeSpec.setMapParam(config);

                    } else {
                        typeSpec.setSingleParam(variable.toString());
                    }
                }
            } else if (typeSpec.getListParam() != null) {
                for (int i = 0; i < typeSpec.getListParam().size(); i++) {
                    if (typeSpec.getListParam().get(i).startsWith("$")) {
                        Object variable = variableMap.get(typeSpec.getListParam().get(i).substring(1));
                        typeSpec.getListParam().set(i, variable.toString());
                    }
                }
            } else if (typeSpec.getMapParam() != null) {
                for (Map.Entry<String, String> entry : typeSpec.getMapParam().entrySet()) {
                    if (entry.getValue().startsWith("$")) {
                        Object variable = variableMap.get(entry.getValue().substring(1));
                        typeSpec.getMapParam().put(entry.getKey(), variable.toString());
                    }
                }
            }

        }

    }


    public boolean isNullValue() {
        return "null".equals(typeSpec.getSingleParam());
    }

    private static interface DefaultHandler {
        void handle(String valueTypeName);
    }

    private static class DefaultHandlerForBuildType implements DefaultHandler {
        private Map<String, List<JsonWrapperNode>> missTypeMap;

        DefaultHandlerForBuildType(Map<String, List<JsonWrapperNode>> missTypeMap) {
            this.missTypeMap = missTypeMap;
        }

        @Override
        public void handle(String valueTypeName) {
            JsonNode jsonNode = new JsonWrapperNode();
            List<JsonWrapperNode> jsonWrapperNodes = missTypeMap.get(valueTypeName);
            if (jsonWrapperNodes == null) {
                missTypeMap.put(valueTypeName, Arrays.asList((JsonWrapperNode) jsonNode));
            } else {
                jsonWrapperNodes.add((JsonWrapperNode) jsonNode);
            }
        }
    }

    private static class DefaultHandlerForBuildJson implements DefaultHandler {

        @Override
        public void handle(String valueTypeName) {
            throw new IllegalArgumentException("Unknown value type name " + valueTypeName);
        }
    }
}
