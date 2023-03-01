package no.ssb.jsontemplate.valueproducer;

import no.ssb.jsontemplate.jsonbuild.JsonNode;

import java.util.List;
import java.util.Map;

/**
 * All node producers should implement this interface. The node producer
 * produces a json node which can print expected json string.
 * <p>
 * JsonTemplate has implemented some basic node producers which can suit
 * most of the cases.
 * <p>
 * Other libraries, such as Guava, Apache Commons, JFaker, support
 * powerful data generations. Users are freely to customize, extend,
 * and add their own producers.
 *
 * @param <T> the type of json node expected to be produced
 */
public interface IValueProducer<T extends JsonNode> {

    /**
     * Returns the name of the producer used in the template.
     *
     * @return the name of the value producer
     */
    String getTypeName();

    /**
     * Produces a node without any parameter.
     * None parameter indicates producing a random value by default.
     *
     * @return produced json node
     */
    T produce();

    /**
     * Produces a node with a single parameter.
     * The single parameter indicates producing a fixed value by default.
     *
     * @param value the single parameter
     * @return produced json node
     */
    T produce(String value);

    /**
     * Produces a node with a list parameter.
     * The list parameter indicates selecting a value from the list by default.
     *
     * @param valueList the list of enumerated values
     * @return produced json node
     */
    T produce(List<String> valueList);

    /**
     * Produces a node with a map parameter.
     * The map parameter is the configuration of producing.
     *
     * @param paramMap the configuration
     * @return produced json node
     */
    T produce(Map<String, String> paramMap);
}
