package no.ssb.jsontemplate.jsonbuild;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class JsonArrayNodeTest {

    @Test
    @DisplayName("convert a collection to a json node")
    void testOfCollection() {
        List<Object> objects = new ArrayList<>();
        objects.add("string");
        objects.add(123);
        objects.add(123f);
        objects.add(true);
        objects.add(null);

        JsonArrayNode jsonNode = JsonArrayNode.of(objects);
        String json = jsonNode.compactString();
        System.out.println(json);

        DocumentContext document = JsonPath.parse(json);
        assertThat(document.read("$[0]", String.class), is("string"));
        assertThat(document.read("$[1]", Integer.class), is(123));
        assertThat(document.read("$[2]", Float.class), is(123f));
        assertThat(document.read("$[3]", Boolean.class), is(true));
        assertThat(document.read("$[4]"), is(nullValue()));
    }

    @Test
    @DisplayName("convert an array to a json node")
    void testOfArray() {
        Object[] objects = new Object[] {
            "string", 123, 123f, true, null
        };

        JsonArrayNode jsonNode = JsonArrayNode.of(objects);
        String json = jsonNode.compactString();
        System.out.println(json);

        DocumentContext document = JsonPath.parse(json);
        assertThat(document.read("$[0]", String.class), is("string"));
        assertThat(document.read("$[1]", Integer.class), is(123));
        assertThat(document.read("$[2]", Float.class), is(123f));
        assertThat(document.read("$[3]", Boolean.class), is(true));
        assertThat(document.read("$[4]"), is(nullValue()));
    }
}
