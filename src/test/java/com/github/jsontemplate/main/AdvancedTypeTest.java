package com.github.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;

import static com.github.jsontemplate.test.ParserUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class AdvancedTypeTest {

    @Test
    void test_ip() {
        DocumentContext document = parse("{ipField : @ip}");
        assertThat(document.read("$.ipField", String.class), is(notNullValue()));
    }

    @Test
    void test_ipv6() {
        DocumentContext document = parse("{ipv6Field : @ipv6}");
        assertThat(document.read("$.ipv6Field", String.class), is(notNullValue()));
    }

    @Test
    void test_base64() {
        DocumentContext document = parse("{base64Field : @base64}");
        assertThat(document.read("$.base64Field", String.class), is(notNullValue()));
    }
}
