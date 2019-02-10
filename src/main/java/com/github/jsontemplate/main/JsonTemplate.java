package com.github.jsontemplate.main;

import com.github.jsontemplate.antlr4.JsonTemplateAntlrLexer;
import com.github.jsontemplate.antlr4.JsonTemplateAntlrParser;
import com.github.jsontemplate.jsonbuild.JsonBuilder;
import com.github.jsontemplate.jsonbuild.JsonNode;
import com.github.jsontemplate.jsonbuild.JsonWrapperNode;
import com.github.jsontemplate.modelbuild.JsonTemplateTreeListener;
import com.github.jsontemplate.modelbuild.SimplePropertyDeclaration;
import com.github.jsontemplate.modelbuild.handler.DefaultJsonBuildHandler;
import com.github.jsontemplate.modelbuild.handler.DefaultTypeBuildHandler;
import com.github.jsontemplate.valueproducer.Base64NodeProducer;
import com.github.jsontemplate.valueproducer.BooleanNodeProducer;
import com.github.jsontemplate.valueproducer.FloatNodeProducer;
import com.github.jsontemplate.valueproducer.INodeProducer;
import com.github.jsontemplate.valueproducer.IntegerNodeProducer;
import com.github.jsontemplate.valueproducer.IpNodeProducer;
import com.github.jsontemplate.valueproducer.Ipv6NodeProducer;
import com.github.jsontemplate.valueproducer.RawStringNodeProducer;
import com.github.jsontemplate.valueproducer.StringNodeProducer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTemplate {

    private String template;
    private Map<String, Object> variableMap = new HashMap<>();
    private Map<String, JsonNode> variableNodeMap = new HashMap<>();
    private Map<String, INodeProducer> producerMap = new HashMap<>();
    private JsonNode builtJsonNode;

    public JsonTemplate(String template) {
        this.template = template;
        initializeProducerMap();
    }

    protected void initializeProducerMap() {
        producerMap.put("s", new StringNodeProducer());
        producerMap.put("i", new IntegerNodeProducer());
        producerMap.put("b", new BooleanNodeProducer());
        producerMap.put("f", new FloatNodeProducer());
        producerMap.put("ip", new IpNodeProducer());
        producerMap.put("ipv6", new Ipv6NodeProducer());
        producerMap.put("base64", new Base64NodeProducer());
        producerMap.put("raw", new RawStringNodeProducer());
    }

    public JsonTemplate withVar(String variableName, Object variable) {
        this.variableMap.put(variableName, variable);
        return this;
    }

    public JsonTemplate withVars(Map<String, Object> variables) {
        variables.forEach(this::withVar);
        return this;
    }

    public JsonTemplate withNodeProducer(String name, INodeProducer nodeProducer) {
        this.producerMap.put(name, nodeProducer);
        return this;
    }

    public JsonNode parse() {
        if (template == null) {
            throw new IllegalArgumentException("Template is not set.");
        }
        if (builtJsonNode == null) {
            builtJsonNode = buildJsonNode(template);
        }
        return builtJsonNode;
    }

    private JsonNode buildJsonNode(String template) {
        buildVariableNodeMap();

        SimplePropertyDeclaration rootDeclaration = stringToJsonTemplateModel(template);
        Map<String, JsonNode> typeMap = buildTypeMap(rootDeclaration);
        rootDeclaration.applyVariablesToParameters(variableMap);

        JsonBuilder builder = new JsonBuilder();
        rootDeclaration.buildJsonTemplate(builder, producerMap, typeMap, variableNodeMap, new DefaultJsonBuildHandler());

        return builder.build();
    }

    private void buildVariableNodeMap() {
        variableMap.forEach((key, value) -> this.variableNodeMap.put(key, JsonNode.of(value)));
    }

    private SimplePropertyDeclaration stringToJsonTemplateModel(String template) {
        JsonTemplateAntlrLexer jsonTemplateLexer = new JsonTemplateAntlrLexer(new ANTLRInputStream(template));
        CommonTokenStream commonTokenStream = new CommonTokenStream(jsonTemplateLexer);
        JsonTemplateAntlrParser parser = new JsonTemplateAntlrParser(commonTokenStream);

        JsonTemplateTreeListener listener = new JsonTemplateTreeListener();

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(listener, parser.root());
        SimplePropertyDeclaration rootDeclaration = listener.getRoot();
        return rootDeclaration;
    }

    private Map<String, JsonNode> buildTypeMap(SimplePropertyDeclaration rootDeclaration) {
        List<SimplePropertyDeclaration> typeDeclList = new ArrayList<>();
        rootDeclaration.collectTypeDeclaration(typeDeclList);

        for (SimplePropertyDeclaration typeDecl : typeDeclList) {
            typeDecl.getParent().removeProperty(typeDecl);
            typeDecl.setParent(null);
        }

        Map<String, JsonNode> typeMap = buildTypeMap(producerMap, typeDeclList);
        return typeMap;
    }

    private Map<String, JsonNode> buildTypeMap(Map<String, INodeProducer> producerMap, List<SimplePropertyDeclaration> typeDeclarations) {
        Map<String, JsonNode> typeMap = new HashMap<>();
        Map<String, List<JsonWrapperNode>> missTypeMap = new HashMap<>();
        for (SimplePropertyDeclaration typeDecl : typeDeclarations) {
            JsonBuilder jsonBuilder = new JsonBuilder();
            typeDecl.buildJsonTemplate(jsonBuilder, producerMap, typeMap,
                    variableNodeMap, new DefaultTypeBuildHandler(missTypeMap));
            JsonNode typeNode = jsonBuilder.build();
            typeMap.put(typeDecl.getPropertyName().substring(1), typeNode);
        }
        for (Map.Entry<String, List<JsonWrapperNode>> entry : missTypeMap.entrySet()) {
            JsonNode jsonNode = typeMap.get(entry.getKey());
            for (JsonWrapperNode jsonWrapperNode : entry.getValue()) {
                jsonWrapperNode.setJsonNode(jsonNode);
            }
        }
        return typeMap;
    }

}
