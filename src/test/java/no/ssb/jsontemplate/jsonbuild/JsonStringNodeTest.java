package no.ssb.jsontemplate.jsonbuild;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class JsonStringNodeTest {

    @ParameterizedTest
    @ValueSource(strings = {"abcde", "", " ", "!@#$%^&*()+-=\\/\n\t"})
    void testOf(String value) {
        JsonStringNode node = JsonStringNode.of(value);
        String printedValue = node.compactString();

        assertThat(printedValue, is("\"" + value + "\""));
    }
}