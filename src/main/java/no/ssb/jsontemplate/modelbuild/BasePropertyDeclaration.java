package no.ssb.jsontemplate.modelbuild;

import no.ssb.jsontemplate.jsonbuild.*;
import no.ssb.jsontemplate.modelbuild.handler.DefaultBuildHandler;
import no.ssb.jsontemplate.valueproducer.IValueProducer;

import java.util.*;
import java.util.stream.Collectors;

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

    public void buildJsonTemplate(JsonBuilder builder,
                                  Map<String, IValueProducer> producerMap,
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

    protected TypeSpec getDefaultTypeSpec(String defaultTypeName) {
        TypeSpec defaultTypeSpec = new TypeSpec();
        defaultTypeSpec.setTypeName(defaultTypeName);
        return defaultTypeSpec;
    }

    protected JsonNode buildNodeFromProducer(Map<String, IValueProducer> producerMap) {
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

    protected void buildChildrenJsonTemplate(JsonBuilder builder, Map<String, IValueProducer> producerMap,
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
