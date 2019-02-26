package com.github.jsontemplate.test;

import com.github.jsontemplate.main.JsonTemplate;
import com.github.jsontemplate.valueproducer.INodeProducer;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.util.Map;

public class ParserUtils {

    public static DocumentContext parse(String template) {
        String json = new JsonTemplate(template).prettyString();
        comparePrint(template, json);
        return JsonPath.parse(json);
    }

    public static DocumentContext parse(String template, Map<String, Object> variableMap) {
        String json = new JsonTemplate(template).withVars(variableMap).prettyString();
        comparePrint(template, json);
        return JsonPath.parse(json);
    }

    public static DocumentContext parse(String template, INodeProducer producer) {
        String json = new JsonTemplate(template).withNodeProducer(producer).prettyString();
        comparePrint(template, json);
        return JsonPath.parse(json);
    }

    private static void comparePrint(String template, String generatedJson) {
        System.out.println("===== Template =====");
        System.out.println(template);
        System.out.println("===== Generated Json =====");
        System.out.println(generatedJson);
    }
}
