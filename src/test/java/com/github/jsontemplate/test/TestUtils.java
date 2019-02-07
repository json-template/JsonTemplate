package com.github.jsontemplate.test;

import com.github.jsontemplate.jsonbuild.JsonNode;
import com.github.jsontemplate.main.JsonTemplate;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;


import java.util.Map;

public class TestUtils {

    public static DocumentContext parse(String template) {
        System.out.println("===== Template =====");
        System.out.println(template);
        JsonNode jsonNode = new JsonTemplate().parse(template);
        String json = jsonNode.prettyPrint(0);
        System.out.println("===== Generated Json =====");
        System.out.println(json);
        return JsonPath.parse(json);
    }

    public static DocumentContext parse(String template, Map<String, Object> variableMap) {
        System.out.println("===== Template =====");
        System.out.println(template);
        JsonNode jsonNode = new JsonTemplate().withVariables(variableMap).parse(template);
        String json = jsonNode.prettyPrint(0);
        System.out.println("===== Generated Json =====");
        System.out.println(json);
        return JsonPath.parse(json);
    }
}
