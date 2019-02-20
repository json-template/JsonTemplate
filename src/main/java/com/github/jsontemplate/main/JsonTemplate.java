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
 * String template = "{city:Utrecht,street:Musicallaan,number:@i(413)}"
 * String json = new JsonTemplate(template).parse().print();<pre/>
 * <br/>
 * If you need only a json which is schema compatible, the template can be specified as:
 * <pre>
 * String template = "{city,street,number:@i}"<pre/>
 *
 * <code>@i</code> refers to a value producer. You can freely extend or add value producers
 * to suit your needs.
 *
 * @see <a href="https://github.com/json-template/JsonTemplate">JsonTemplate GitHub<a/>
 */
public class JsonTemplate {

    protected String template;
    protected Map<String, Object> variableMap = new HashMap<>();
    protected Map<String, INodeProducer> producerMap = new HashMap<>();

    private Map<String, JsonNode> variableNodeMap = new HashMap<>();
    private JsonNode rootNode;

    public JsonTemplate(String template) {
        this.template = template;
        initializeProducerMap();
    }

    /**
     * Registers a list of pre-installed value producers. A pre-installed value producer
     * can be replaced a customized one by using the same key.
     *
     * A customized one can be also mapped to a new key. For example:
     * <pre>producerMap.put("temperature", new TemperatureNodeProducer())</pre>
     */
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

    /**
     * Registers a variable which is used in the template.
     * Example:
     * <pre>
     * String json = new JsonTemplate("{city:$cityVar}")
     *                .withVar("cityVar", "Utrecht").parse().print();
     * </pre>
     * In the end, the value of json is
     * <code>{"city":"Utrecht"}</code>
     * <p/>
     * The variable can be set as a single parameter. For example,
     * <code>{city:@s($var)}</code>
     * If var is a collection or an array with values, e.g., {"Amsterdam", "Utrecht"}.
     * The template is equal to the list parameter form {city:@s(Amsterdam, Utrecht)}
     * which selects a value from the list parameter.
     * <p/>
     * If var is a map, e.g., mapVar.put("length", 10). The template is equal to the
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
     * @param variable variable value
     * @return
     */
    public JsonTemplate withVar(String variableName, Object variable) {
        this.variableMap.put(variableName, variable);
        return this;
    }

    /**
     * Registers a map of variables which are used in the template.
     *
     * @see #withVar(String, Object)
     *
     * @param variables map of variables
     * @return
     */
    public JsonTemplate withVars(Map<String, Object> variables) {
        variables.forEach(this::withVar);
        return this;
    }

    public JsonTemplate withNodeProducer(String name, INodeProducer nodeProducer) {
        this.producerMap.put(name, nodeProducer);
        return this;
    }

    /**
     * Produces json string based on the given information, such as template string,
     * variables, and customized producers. Each execution
     * produces a json string with different random values, if it has.
     *
     * @return a compact json string
     */
    public String print() {
        parse();
        return rootNode.print();
    }

    /**
     * @see #print()
     *
     * @return a json string with identation
     */
    public String prettyPrint() {
        parse();
        return rootNode.prettyPrint(0);
    }

    private void parse() {
        if (template == null) {
            throw new IllegalArgumentException("Template is not set.");
        }
        if (rootNode == null) {
            rootNode = buildJsonNode(template);
        }
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
