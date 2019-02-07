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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimplePropertyDeclaration {
    protected String propertyName;
    protected TypeSpec typeSpec = new TypeSpec();
    protected List<SimplePropertyDeclaration> properties = new ArrayList<>();
    protected SimplePropertyDeclaration parent;
    protected TypeSpec arrayTypeSpec = new TypeSpec();

    public TypeSpec getTypeSpec() {
        return typeSpec;
    }

    public TypeSpec getArrayTypeSpec() {
        return arrayTypeSpec;
    }

    public List<SimplePropertyDeclaration> getProperties() {
        return properties;
    }


    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }


    public void addProperty(SimplePropertyDeclaration propertyDeclaration) {
        this.properties.add(propertyDeclaration);
        propertyDeclaration.setParent(this);
    }

    public void removeProperty(SimplePropertyDeclaration propertyDeclaration) {
        this.properties.remove(propertyDeclaration);
        propertyDeclaration.setParent(null);
    }

    public SimplePropertyDeclaration getParent() {
        return parent;
    }

    public void setParent(SimplePropertyDeclaration parent) {
        this.parent = parent;
    }

    ArrayPropertyDeclaration asArrayProperty() {
        return new ArrayPropertyDeclaration(this.propertyName);
    }

    ObjectPropertyDeclaration asObjectProperty() {
        return new ObjectPropertyDeclaration(this.propertyName);
    }

    public void collectTypeDeclaration(List<SimplePropertyDeclaration> typeList) {
        if (isTypeDeclaration()) {
            typeList.add(this);
        }
        for (SimplePropertyDeclaration declaration : properties) {
            declaration.collectTypeDeclaration(typeList);
        }
    }

    public void buildJsonTemplate(JsonBuilder builder, Map<String, INodeProducer> producerMap,
                                  Map<String, JsonNode> typeMap,
                                  Map<String, JsonNode> variableMap, DefaultBuildHandler defaultHandler) {
        JsonNode jsonNode = null;
        if (isNullValue()) {
            jsonNode = new JsonNullNode();
        } else {
            jsonNode = findJsonNodeFromVariable(variableMap, typeSpec.getTypeName());
            if (jsonNode == null) { // it is not a variable, search type map
//                if (typeSpec.getTypeName() == null && typeSpec.getSingleParam() == null) {
//                    findDefaultJsonNode();
//
//                    if (jsonNode == null) { // cannot find any matched type
//                        defaultHandler.handle(valueTypeName);
//                    }
//                }

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

    private void putOrAddNode(JsonBuilder builder, JsonNode jsonNode) {
        if (builder.inObject()) {
            builder.putNode(propertyName, jsonNode);
        } else if (builder.inArray()) {
            builder.addNode(jsonNode);
        }
    }

    protected String findValueType() {
        String valueTypeName = typeSpec.getTypeName();
        SimplePropertyDeclaration declParent = this.getParent();
        while (valueTypeName == null && declParent != null) {
            valueTypeName = declParent.getTypeSpec().getTypeName();
            declParent = declParent.getParent();
        }
        if (valueTypeName == null) {
            valueTypeName = "s"; // todo improve, temporary solution for array default type
        }
        return valueTypeName;
    }

    protected JsonNode buildNodeFromProducer(Map<String, INodeProducer> producerMap, String valueTypeName) {
        JsonNode jsonNode = null;
        INodeProducer producer = producerMap.get(valueTypeName);
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

    protected void handleComposite(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, List<JsonWrapperNode>> missTypeMap, Map<String, JsonNode> variableMap) {
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

    protected void buildChildrenJsonTemplate(JsonBuilder builder, Map<String, INodeProducer> producerMap,
                                             Map<String, JsonNode> typeMap,
                                             Map<String, JsonNode> variableMap, DefaultBuildHandler defaultHandler) {
        for (SimplePropertyDeclaration declaration : properties) {
            declaration.buildJsonTemplate(builder, producerMap, typeMap, variableMap, defaultHandler);
        }
    }

    boolean isTypeDeclaration() {
        return propertyName != null && propertyName.startsWith("@");
    }

    private JsonNode findJsonNodeFromVariable(Map<String, JsonNode> variableMap, String name) {
        if (name != null && name.startsWith("$")) {
            return variableMap.get(name.substring(1));
        }
        return null;
    }


    public void applyVariables(Map<String, Object> variableMap) {
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

    public boolean isNullValue() {
        return "null".equals(typeSpec.getSingleParam());
    }

    protected boolean isRoot() {
        return parent == null;
    }
}
