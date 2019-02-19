package com.github.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.jsontemplate.test.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;


class VariableTest {

    @Test
    void test_stringVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("name", "John");
        DocumentContext document = parse("{name : $name}", varMap);
        assertThat(document.read("$.name", String.class), is("John"));
    }

    @Test
    void test_integerVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("age", 20);
        DocumentContext document = parse("{age : $age}", varMap);
        assertThat(document.read("$.age", Integer.class), is(20));
    }

    @Test
    void test_booleanVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("male", true);
        DocumentContext document = parse("{male : $male}", varMap);
        assertThat(document.read("$.male", Boolean.class), is(true));
    }

    @Test
    void test_arrayVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("letters", new String[]{"A", "B", "C"});
        DocumentContext document = parse("{letters : $letters}", varMap);
        assertThat(document.read("$.letters[0]", String.class), is("A"));
        assertThat(document.read("$.letters[1]", String.class), is("B"));
        assertThat(document.read("$.letters[2]", String.class), is("C"));
    }

    @Test
    void test_listVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("letters", Arrays.asList("A", "B", "C"));
        DocumentContext document = parse("{letters : $letters}", varMap);
        assertThat(document.read("$.letters[0]", String.class), is("A"));
        assertThat(document.read("$.letters[1]", String.class), is("B"));
        assertThat(document.read("$.letters[2]", String.class), is("C"));
    }

    @Test
    void test_mapVariable() {

        Map<String, Object> person = new HashMap<>();
        person.put("name", "John");
        person.put("age", 20);
        person.put("male", true);
        person.put("roles", Arrays.asList("Admin", "Finance", "HR"));

        Map<String, Object> varMap = new HashMap<>();
        varMap.put("person", person);

        parse("{person : $person}", varMap);
    }

    @Test
    void test_objectInSingleParam() {
        Map<String, Object> variables = new HashMap<>();
        String value = "helloworld";
        variables.put("myValue", value);
        DocumentContext document = parse("{aField: @s($myValue)}", variables);
        assertThat(document.read("$.aField", String.class), is(value));
    }

    @Test
    void test_listInSingleParam() {
        Map<String, Object> variables = new HashMap<>();
        List<String> value = Arrays.asList("A", "B", "C", "D");
        variables.put("myValue", value);
        DocumentContext document = parse("{aField: @s($myValue)}", variables);
        assertThat(document.read("$.aField", String.class), isIn(value));
    }

    @Test
    void test_mapInSingleParam() {
        Map<String, Object> variables = new HashMap<>();
        Map<String, String> value = new HashMap<>();
        value.put("length", "20");
        variables.put("config", value);
        DocumentContext document = parse("{aField: @s($config)}", variables);
        assertThat(document.read("$.aField", String.class).length(), is(20));
    }

    @Test
    void test_objectInListParam() {
        Map<String, Object> variables = new HashMap<>();
        String value = "hello";
        variables.put("myValue", value);
        DocumentContext document = parse("{aField: @s(A, B, $myValue, D)}", variables);
        assertThat(document.read("$.aField", String.class), isIn(Arrays.asList("A", "B", "hello", "D")));
    }

    @Test
    void test_objectInMapParam() {
        Map<String, Object> variables = new HashMap<>();
        int size = 20;
        variables.put("size", size);
        DocumentContext document = parse("{aField: @s(length=$size)}", variables);
        assertThat(document.read("$.aField", String.class).length(), is(20));
    }
}
