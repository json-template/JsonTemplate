package no.ssb.jsontemplate.templatetests;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import no.ssb.jsontemplate.JsonTemplate;

public class TestUtils {

    public final static int REPEATED_COUNT = 1;
    public final static boolean PRETTY = true;

    public static DocumentContext parse(JsonTemplate jsonTemplate) {
        String json;
        if (PRETTY) {
            json = jsonTemplate.prettyString();
        } else {
            json = jsonTemplate.compactString();
        }
        comparePrint(jsonTemplate.getTemplate(), json);
        return JsonPath.parse(json);
    }

    private static void comparePrint(String template, String generatedJson) {
        System.out.println("===== Template =====");
        System.out.println(template);
        System.out.println("===== Generated Json =====");
        System.out.println(generatedJson);
        System.out.println();
    }
}
