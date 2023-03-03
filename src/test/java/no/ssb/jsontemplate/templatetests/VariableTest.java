package no.ssb.jsontemplate.templatetests;

import com.jayway.jsonpath.DocumentContext;
import no.ssb.jsontemplate.JsonTemplate;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static no.ssb.jsontemplate.templatetests.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


class VariableTest {

    @Test
    void test_stringVariable() {
        JsonTemplate jsonTemplate = new JsonTemplate("{name : $name}").withVar("name", "John");
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.name", String.class), is("John"));
    }

    @Test
    void test_integerVariable() {
        JsonTemplate jsonTemplate = new JsonTemplate("{age : $age}").withVar("age", 20);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.age", Integer.class), is(20));
    }

    @Test
    void test_booleanVariable() {
        JsonTemplate jsonTemplate = new JsonTemplate("{male : $male}").withVar("male", true);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.male", Boolean.class), is(true));
    }

    @Test
    void test_floatVariable() {
        JsonTemplate jsonTemplate = new JsonTemplate("{balance : $balance}").withVar("balance", 1.23f);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.balance", Float.class), is(1.23f));
    }

    @Test
    void test_arrayVariable() {
        JsonTemplate jsonTemplate = new JsonTemplate("{letters : $letters}")
                .withVar("letters", new String[]{"A", "B", "C"});
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.letters[0]", String.class), is("A"));
        assertThat(document.read("$.letters[1]", String.class), is("B"));
        assertThat(document.read("$.letters[2]", String.class), is("C"));
    }

    @Test
    void test_listVariable() {
        JsonTemplate jsonTemplate = new JsonTemplate("{letters : $letters}")
                .withVar("letters", Arrays.asList("A", "B", "C"));
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.letters[0]", String.class), is("A"));
        assertThat(document.read("$.letters[1]", String.class), is("B"));
        assertThat(document.read("$.letters[2]", String.class), is("C"));
    }

    @Test
    void test_mapVariable() {
        String name = "John";
        Integer age = 20;
        Boolean male = true;
        String admin = "Admin";
        List<String> roles = Arrays.asList(admin, "Finance", "HR");
        Map<String, Object> person = new HashMap<>();
        person.put("name", name);
        person.put("age", age);
        person.put("male", male);
        person.put("roles", roles);

        JsonTemplate jsonTemplate = new JsonTemplate("{person : $person}").withVar("person", person);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.person.name", String.class), is(name));
        assertThat(document.read("$.person.age", Integer.class), is(age));
        assertThat(document.read("$.person.male", Boolean.class), is(male));
        assertThat(document.read("$.person.roles", List.class), is(roles));
        assertThat(document.read("$.person.roles[0]", String.class), is(admin));

    }

    @Test
    void test_stringAsSingleParamInStringType() {
        String value = "helloworld";
        JsonTemplate jsonTemplate = new JsonTemplate("{aField: @s($myValue)}").withVar("myValue", value);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.aField", String.class), is(value));
    }

    @Test
    void test_integerAsSingleParamInStringType() {
        int value = 2;
        JsonTemplate jsonTemplate = new JsonTemplate("{aField: @s($myValue)}").withVar("myValue", value);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.aField", String.class), is(Integer.toString(value)));
    }

    @Test
    void test_stringAseSingleParamInIntegerType() {
        String value = "hello";
        JsonTemplate jsonTemplate = new JsonTemplate("{aField: @i($myValue)}").withVar("myValue", value);
        assertThrows(NumberFormatException.class, () -> parse(jsonTemplate));
    }

    @RepeatedTest(TestUtils.REPEATED_COUNT)
    void test_arrayInSingleParam() {
        String[] value = new String[]{"A", "B", "C", "D"};
        JsonTemplate jsonTemplate = new JsonTemplate("{aField: @s($myValue)}").withVar("myValue", value);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.aField", String.class), isIn(value));
    }

    @RepeatedTest(TestUtils.REPEATED_COUNT)
    void test_listInSingleParam() {
        List<String> value = Arrays.asList("A", "B", "C", "D");
        JsonTemplate jsonTemplate = new JsonTemplate("{aField: @s($myValue)}").withVar("myValue", value);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.aField", String.class), isIn(value));
    }

    @RepeatedTest(TestUtils.REPEATED_COUNT)
    void test_multipleVarsInMapParam() {
        JsonTemplate jsonTemplate = new JsonTemplate("{aField: @s(min=$min,max=$max)}")
                .withVar("min", 10)
                .withVar("max", 20);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.aField", String.class).length(),
                is(both(greaterThanOrEqualTo(10)).and(lessThanOrEqualTo(20))));
    }

    @RepeatedTest(TestUtils.REPEATED_COUNT)
    void test_varsMapInMapParam() {
        Map<String, Object> values = new HashMap<>();
        values.put("min", 10);
        values.put("max", 20);
        JsonTemplate jsonTemplate = new JsonTemplate("{aField: @s(min=$min,max=$max)}")
                .withVars(values);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.aField", String.class).length(),
                is(both(greaterThanOrEqualTo(10)).and(lessThanOrEqualTo(20))));
    }

    @RepeatedTest(TestUtils.REPEATED_COUNT)
    void test_mapInSingleParam() {
        Map<String, Object> value = new HashMap<>();
        value.put("min", 10);
        value.put("max", "20");
        JsonTemplate jsonTemplate = new JsonTemplate("{aField: @s($config)}").withVar("config", value);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.aField", String.class).length(),
                is(both(greaterThanOrEqualTo(10)).and(lessThanOrEqualTo(20))));
    }

    @Test
    void test_objectInListParam() {
        String value = "hello";
        JsonTemplate jsonTemplate = new JsonTemplate("{aField: @s(A, B, $myValue, D)}").withVar("myValue", value);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.aField", String.class), isIn(Arrays.asList("A", "B", "hello", "D")));
    }

    @Test
    void test_objectInMapParam() {
        int size = 20;
        JsonTemplate jsonTemplate = new JsonTemplate("{aField: @s(length=$size)}").withVar("size", size);
        DocumentContext document = parse(jsonTemplate);
        assertThat(document.read("$.aField", String.class).length(), is(20));
    }
}
