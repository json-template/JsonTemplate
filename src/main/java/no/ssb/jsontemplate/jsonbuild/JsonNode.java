package no.ssb.jsontemplate.jsonbuild;

import java.util.Collection;
import java.util.Map;

/**
 * Interface for all type of json nodes.
 */
public interface JsonNode {

    /**
     * Creates a JsonNode based on a given object.
     * It supports the following types:
     * <ul>
     * <li>null - converted to JsonNullNode</li>
     * <li>Integer - converted to JsonIntegerNode</li>
     * <li>Float - converted to JsonFloatNode</li>
     * <li>Boolean - converted to JsonBooleanNode</li>
     * <li>String - converted to JsonStringNode</li>
     * <li>Array - converted to JsonArrayNode</li>
     * <li>Collection - converted to JsonArrayNode</li>
     * <li>Map - converted to JsonArrayNode</li>
     * <li>otherwise, it is converted to JsonStringNode with
     * the string representation of the object.
     * </li>
     * </ul>
     *
     * @param obj the object to be converted
     * @return the converted json node
     */
    static JsonNode of(Object obj) {
        JsonNode jsonNode;
        if (obj == null) {
            jsonNode = new JsonNullNode();
        } else if (obj instanceof Integer) {
            jsonNode = JsonIntegerNode.of((Integer) obj);
        } else if (obj instanceof Float) {
            jsonNode = JsonFloatNode.of((Float) obj);
        } else if (obj instanceof Boolean) {
            jsonNode = JsonBooleanNode.of((Boolean) obj);
        } else if (obj instanceof String) {
            jsonNode = JsonStringNode.of((String) obj);
        } else if (obj.getClass().isArray()) {
            jsonNode = JsonArrayNode.of((Object[]) obj);
        } else if (obj instanceof Collection) {
            jsonNode = JsonArrayNode.of((Collection<?>) obj);
        } else if (obj instanceof Map) {
            jsonNode = JsonObjectNode.of((Map) obj);
        } else {
            jsonNode = JsonStringNode.of(obj.toString());
        }
        return jsonNode;
    }

    /**
     * Produces a json string in a compact format.
     *
     * @return a compact string of the json
     */
    String compactString();

    /**
     * Produces a json string with indentations.
     *
     * @param indentation the amount of indentations
     * @return a json string with indentations
     */
    String prettyString(int indentation);
}
