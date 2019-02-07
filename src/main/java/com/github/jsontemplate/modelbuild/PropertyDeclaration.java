package com.github.jsontemplate.modelbuild;

import com.github.jsontemplate.jsonbuild.JsonArrayNode;
import com.github.jsontemplate.jsonbuild.JsonBuilder;
import com.github.jsontemplate.jsonbuild.JsonNode;
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
    protected String typeName;

    protected String singleParam;
    protected List<String> listParam;
    protected Map<String, String> mapParam;
    protected boolean isObject;
    protected List<PropertyDeclaration> properties = new ArrayList<>();
    protected PropertyDeclaration parent;
    protected boolean isArray;

    protected String arraySingleParam;
    protected List<String> arrayListParam;
    protected Map<String, String> arrayMapParam;

    public List<PropertyDeclaration> getProperties() {
        return properties;
    }

    public String getArraySingleParam() {
        return arraySingleParam;
    }

    public void setArraySingleParam(String arraySingleParam) {
        this.arraySingleParam = arraySingleParam;
    }

    public List<String> getArrayListParam() {
        if (arrayListParam == null) {
            arrayListParam = new ArrayList<>();
        }
        return arrayListParam;
    }

    public void setArrayListParam(List<String> arrayListParam) {
        this.arrayListParam = arrayListParam;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        if (typeName != null) {
            this.typeName = typeName;
        }
    }

    public String getSingleParam() {
        return singleParam;
    }

    public void setSingleParam(String singleParam) {
        this.singleParam = singleParam;
    }

    public List<String> getListParam() {
        if (listParam == null) {
            listParam = new ArrayList<>();
        }
        return listParam;
    }

    public void setListParam(List<String> listParam) {
        this.listParam = listParam;
    }

    public Map<String, String> getMapParam() {
        if (mapParam == null) {
            mapParam = new HashMap<>();
        }
        return mapParam;
    }

    public void setMapParam(Map<String, String> mapParam) {
        this.mapParam = mapParam;
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

    public Map<String, String> getArrayMapParam() {
        if (arrayMapParam == null) {
            arrayMapParam = new HashMap<>();
        }
        return arrayMapParam;
    }

    public void setArrayMapParam(Map<String, String> arrayMapParam) {
        this.arrayMapParam = arrayMapParam;
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
        JsonNode jsonNode = findJsonNodeFromVariable(variableMap, typeName);
        if (jsonNode == null) {
            String valueTypeName = findValueType();
            jsonNode = buildNodeFromProducer(producerMap, valueTypeName);

            if (jsonNode == null) {
                jsonNode = typeMap.get(valueTypeName);
            }
            if (jsonNode == null) {
                defaultHandler.handle(valueTypeName);
            }
        }

        putOrAddNode(builder, jsonNode);
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
        String valueTypeName = typeName;
        PropertyDeclaration declParent = this.getParent();
        while (valueTypeName == null && declParent != null) {
            valueTypeName = declParent.getTypeName();
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
            if (singleParam != null) {
                jsonNode = producer.produce(singleParam);
            } else if (listParam != null) {
                jsonNode = producer.produce(listParam);
            } else if (mapParam != null) {
                jsonNode = producer.produce(mapParam);
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
            if (valueTypeName != null) {
                JsonNode jsonNode = buildNodeFromProducer(producerMap, valueTypeName);

                if (jsonNode == null) {
                    jsonNode = typeMap.get(valueTypeName);
                }
                setArrayInfo(builder.peekArrayNode(), jsonNode);
            }
        }
        builder.end();
    }

    private void setArrayInfo(JsonArrayNode jsonArrayNode, JsonNode defaultNode) {
        jsonArrayNode.setDefaultNode(defaultNode);
        if (this.arraySingleParam != null) {
            jsonArrayNode.setParameters(this.arraySingleParam);
        }
        if (this.arrayListParam != null) {
            jsonArrayNode.setParameters(this.arrayListParam);
        }
        if (this.arrayMapParam != null) {
            jsonArrayNode.setParameters(this.arrayMapParam);
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
            if (singleParam != null) {
                if (singleParam.startsWith("$")) {
                    Object variable = variableMap.get(singleParam.substring(1));
                    if (variable instanceof Collection<?>) {
                        Collection<?> collectionVariable = (Collection<?>) variable;
                        singleParam = null;
                        listParam = collectionVariable.stream()
                                .map(Object::toString)
                                .collect(Collectors.toList());
                        // todo primitives
                    } else if (variable.getClass().isArray()) {
                        Object[] arrayVariable = (Object[]) variable;
                        singleParam = null;
                        listParam = Arrays.stream(arrayVariable)
                                .map(Object::toString)
                                .collect(Collectors.toList());
                        // todo primitives
                    } else if (variable instanceof Map) {
                        singleParam = null;
                        listParam = null;
                        Map<String, Object> mapVariable = (Map<String, Object>) variable;
                        mapParam = mapVariable.entrySet().stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));

                    } else {
                        singleParam = variable.toString();
                    }
                }
            } else if (listParam != null) {
                for (int i = 0; i < listParam.size(); i++) {
                    if (listParam.get(i).startsWith("$")) {
                        Object variable = variableMap.get(listParam.get(i).substring(1));
                        listParam.set(i, variable.toString());
                    }
                }
            } else if (mapParam != null) {
                for (Map.Entry<String, String> entry : mapParam.entrySet()) {
                    if (entry.getValue().startsWith("$")) {
                        Object variable = variableMap.get(entry.getValue().substring(1));
                        mapParam.put(entry.getKey(), variable.toString());
                    }
                }
            }

        }

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
