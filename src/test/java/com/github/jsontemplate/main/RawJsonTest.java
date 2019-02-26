package com.github.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.github.jsontemplate.test.ParserUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RawJsonTest {

    @Test
    void test_rawStringJson() {
        String rawContent = "{\n" +
                "  \"jsonField\" : \"C\"\n" +
                "}";

        DocumentContext document = parse("{aField : @raw(`" + rawContent + "`)}");
        assertThat(document.read("$.aField.jsonField", String.class), is("C"));
    }

    @Test
    void test_rawStringJsonWithVariable() {
        String rawContent = "{\n" +
                "  \"jsonField\" : \"C\"\n" +
                "}";

        DocumentContext document = parse("{aField : @raw($jsonContent)}",
                Collections.singletonMap("jsonContent", rawContent));
        assertThat(document.read("$.aField.jsonField", String.class), is("C"));
    }

}
