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

package com.github.jsontemplate;

import com.github.jsontemplate.antlr4.JsonTemplateAntlrLexer;
import com.github.jsontemplate.antlr4.JsonTemplateAntlrParser;
import com.github.jsontemplate.jsonbuild.JsonBuilder;
import com.github.jsontemplate.jsonbuild.JsonNode;
import com.github.jsontemplate.jsonbuild.JsonWrapperNode;
import com.github.jsontemplate.modelbuild.JsonTemplateTreeListener;
import com.github.jsontemplate.modelbuild.SimplePropertyDeclaration;
import com.github.jsontemplate.modelbuild.handler.DefaultJsonBuildHandler;
import com.github.jsontemplate.modelbuild.handler.DefaultTypeBuildHandler;
import com.github.jsontemplate.valueproducer.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JsonTemplate is for generating a json string based on a schema specification.
 * <br/>
 * For example, to have a json like the following one
 * <pre>
 * {
 *   "city" : "Utrecht",
 *   "street" : "Musicallaan",
 *   "number" : 413
 * }<pre/>
 * You can use the following code snippet to create the expected json.
 * <pre>
 * String template = "{city:Utrecht, street:Musicallaan, number:413}"
 * String json = new JsonTemplate(template).prettyString();<pre/>
 * <br/>
 * If you need only a json which is schema compatible, the template can be specified as:
 * <pre>
 * String template = "{city:@s, street:@s, number:@i}"<pre/>
 *
 * <code>@s</code>, <code>@i</code> refer to value producers.
 *
 * @see <a href="https://github.com/json-template/JsonTemplate">JsonTemplate GitHub<a/>
 */
public class JsonTemplate {

    private String defaultTypeName = SmartNodeProducer.TYPE_NAME;
    private String template;
    private Map<String, Object> variableMap = new HashMap<>(32);
    private Map<String, INodeProducer> producerMap = new HashMap<>(32);
    private Map<String, JsonNode> variableNodeMap = new HashMap<>(32);
    private JsonNode rootNode;

    public JsonTemplate(String template) {
        this.template = template;
        initializeProducerMap();
    }

    /**
     * Registers a variable which is used in the template.
     * Example:
     * <pre>
     * String json = new JsonTemplate("{city:$cityVar}")
     *                .withVar("cityVar", "Utrecht").compactString();
     * </pre>
     * In the end, the value of json is
     * <code>{"city":"Utrecht"}</code>
     * <p/>
     * The variable can be set as a single parameter. For example,
     * <code>{city:@s($var)}</code>
     * If var refers to a collection or an array, e.g., {"Amsterdam", "Utrecht"}.
     * The template is equal to the list parameter form {city:@s(Amsterdam, Utrecht)}
     * which selects one value from the list parameter.
     * <p/>
     * If var refers to a map, e.g., mapVar.put("length", 10). The template is equal to the
     * map parameter form {city:@s(length=10)}.
     * <p/>
     * Otherwise, the string representation of the variable is used.
     * <p/>
     * The variable can be set as an element of the list parameter. For example,
     * <code>{city:@s(Amsterdam, $anotherCity)}</code>. No matter what the type
     * of $anotherCity is, the string representation of the variable is always
     * used.
     * <p/>
     * The variable can be set as a value in the map parameter. For example,
     * <code>{city:@s(length=$expectedLength)}</code>. No matter what the type
     * of $expectedLength is, the string representation of the variable is always
     * used.
     * <p/>
     * The variable can be set as a json value. For example,
     * <code>{city:$cityVar}</code>
     * <p/>
     * If $cityVar is null, it is converted to a json null value.
     * <br/>
     * If $cityVar is an integer or a float, it is converted to a json numeric value.
     * <br/>
     * If $cityVar is a boolean, it is converted to a json boolean value.
     * <br/>
     * If $cityVar is an array or a collection, it is converted to a json array.
     * <br/>
     * If $cityVar is a map, it is converted to a json object.
     * <br/>
     * Otherwise, it is converted to a json string object.
     *
     * @param variableName name of the variable without the leading '$'
     * @param variable     variable value
     * @return
     */
    public JsonTemplate withVar(String variableName, Object variable) {
        this.variableMap.put(variableName, variable);
        return this;
    }

    /**
     * Registers a map of variables which are used in the template.
     *
     * @param variables map of variables
     * @return
     * @see #withVar(String, Object)
     */
    public JsonTemplate withVars(Map<String, Object> variables) {
        variables.forEach(this::withVar);
        return this;
    }

    /**
     * Registers a node producer. The node producer can use a new type name and
     * it can also overwrite a pre-installed one.
     * <p/>
     * The pre-installed node producers are:
     * <ul>
     *  <li>{@link SmartNodeProducer}</li>
     *  <li>{@link StringNodeProducer}</li>
     *  <li>{@link IntegerNodeProducer}</li>
     *  <li>{@link BooleanNodeProducer}</li>
     *  <li>{@link FloatNodeProducer}</li>
     *  <li>{@link IpNodeProducer}</li>
     *  <li>{@link Ipv6NodeProducer}</li>
     *  <li>{@link Base64NodeProducer}</li>
     *  <li>{@link RawStringNodeProducer}</li>
     * <ul/>
     *
     * @param nodeProducer
     * @return
     */
    public JsonTemplate withNodeProducer(INodeProducer nodeProducer) {
        this.addProducer(nodeProducer);
        return this;
    }

    /**
     * Registers the default type name. If the type of a json value is not specified
     * in the template. It searches through its parents util it finds a parent who has
     * as default type or it reaches the root. If the default type of the root is not
     * explicitly specified. The default type is {@link SmartNodeProducer}.
     *
     * Example:
     * <pre>
     *     { obj1: @i{fieldA}, obj2: {fieldB} }
     * <pre/>
     *
     * the default type of root is not specified, by default it is @smart
     * <br/>
     * the default type of obj1 is specified, it is @i
     * <br/>
     * the default type of obj2 is not specified, it searches its parent and finds the default type @smart
     *
     * @param typeName
     * @return
     */
    public JsonTemplate withDefaultTypeName(String typeName) {
        this.defaultTypeName = typeName;
        return this;
    }

    public String getTemplate() {
        return template;
    }

    /**
     * Produces a compact json string.
     *
     * @return a compact json string
     */
    public String compactString() {
        build();
        return rootNode.compactString();
    }

    /**
     * Produces a json string with identations.
     *
     * @return a json string with identation
     * @see #compactString()
     */
    public String prettyString() {
        build();
        return rootNode.prettyString(0);
    }


    private void initializeProducerMap() {
        INodeProducer[] producers = new INodeProducer[]{
                new SmartNodeProducer(),
                new StringNodeProducer(),
                new IntegerNodeProducer(),
                new BooleanNodeProducer(),
                new FloatNodeProducer(),
                new IpNodeProducer(),
                new Ipv6NodeProducer(),
                new Base64NodeProducer(),
                new RawStringNodeProducer()
        };

        Arrays.stream(producers).forEach(this::addProducer);
    }

    private void addProducer(INodeProducer producer) {
        producerMap.put(producer.getTypeName(), producer);
    }

    private void build() {
        if (template == null) {
            throw new IllegalArgumentException("Template is not set.");
        }
        if (rootNode == null) {
            rootNode = buildJsonNode(template);
        }
    }

    private JsonNode buildJsonNode(String template) {
        buildVariableNodeMap();

        JsonTemplateTreeListener listener = parse(template);
        SimplePropertyDeclaration rootDeclaration = listener.getJsonRoot();
        Map<String, JsonNode> typeMap = buildTypeMap(listener.getTypeDefinitionList());
        rootDeclaration.applyVariablesToParameters(variableMap);

        JsonBuilder builder = new JsonBuilder();
        rootDeclaration.buildJsonTemplate(builder, producerMap, typeMap, variableNodeMap, defaultTypeName, new DefaultJsonBuildHandler());

        return builder.build();
    }

    private void buildVariableNodeMap() {
        variableMap.forEach((key, value) -> this.variableNodeMap.put(key, JsonNode.of(value)));
    }

    private JsonTemplateTreeListener parse(String template) {
        JsonTemplateAntlrLexer jsonTemplateLexer = new JsonTemplateAntlrLexer(CharStreams.fromString(template));
        CommonTokenStream commonTokenStream = new CommonTokenStream(jsonTemplateLexer);
        JsonTemplateAntlrParser parser = new JsonTemplateAntlrParser(commonTokenStream);

        JsonTemplateTreeListener listener = new JsonTemplateTreeListener();

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(listener, parser.root());
        return listener;
    }

    private Map<String, JsonNode> buildTypeMap(List<SimplePropertyDeclaration> typeDeclarations) {
        Map<String, JsonNode> typeMap = new HashMap<>();
        Map<String, List<JsonWrapperNode>> missTypeMap = new HashMap<>();
        for (SimplePropertyDeclaration typeDecl : typeDeclarations) {
            JsonBuilder jsonBuilder = new JsonBuilder();
            typeDecl.buildJsonTemplate(jsonBuilder, producerMap, typeMap,
                    variableNodeMap, defaultTypeName, new DefaultTypeBuildHandler(missTypeMap));
            JsonNode typeNode = jsonBuilder.build();
            typeMap.put(typeDecl.getPropertyName(), typeNode);
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