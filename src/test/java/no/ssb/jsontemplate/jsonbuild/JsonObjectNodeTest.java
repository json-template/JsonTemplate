package no.ssb.jsontemplate.jsonbuild;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class JsonObjectNodeTest {

    @Test
    @DisplayName("convert a map to a json node")
    void testOf() {
        Map<String, Object> map = new HashMap<>();
        map.put("strField", "string");
        map.put("intField", 123);
        map.put("floatField", 123f);
        map.put("boolField", true);
        map.put("nullField", null);
        JsonObjectNode node = JsonObjectNode.of(map);
        String printedValue = node.compactString();
        System.out.println(printedValue);

        DocumentContext document = JsonPath.parse(printedValue);
        assertThat(document.read("$.strField", String.class), is("string"));
        assertThat(document.read("$.intField", Integer.class), is(123));
        assertThat(document.read("$.floatField", Float.class), is(123f));
        assertThat(document.read("$.boolField", Boolean.class), is(true));
        assertThat(document.read("$.nullField"), is(nullValue()));
    }

}
