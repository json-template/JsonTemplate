package com.github.jsontemplate.test;

import com.github.jsontemplate.main.JsonTemplate;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class ParserUtils {

    public static DocumentContext parse(JsonTemplate jsonTemplate) {
        String json = jsonTemplate.prettyString();
        comparePrint(jsonTemplate.getTemplate(), json);
        return JsonPath.parse(json);
    }

    private static void comparePrint(String template, String generatedJson) {
        System.out.println("===== Template =====");
        System.out.println(template);
        System.out.println("===== Generated Json =====");
        System.out.println(generatedJson);
    }
}
