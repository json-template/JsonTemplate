package no.ssb.jsontemplate.jsonbuild;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represents a producer of a json object value.
 */
public final class JsonObjectNode implements JsonNode {

    private Map<String, JsonNode> children = new LinkedHashMap<>();

    /**
     * Creates a JsonObjectNode with a given map.
     *
     * @param valueMap the map value to be converted
     * @return the converted json object node
     */
    public static JsonObjectNode of(Map<String, ?> valueMap) {
        JsonObjectNode jsonObjectNode = new JsonObjectNode();
        valueMap.forEach((key, value) -> jsonObjectNode.putNode(key, JsonNode.of(value)));
        return jsonObjectNode;
    }

    /**
     * Adds a child
     *
     * @param key  name
     * @param node value
     */
    public void putNode(String key, JsonNode node) {
        this.children.put(key, node);
    }

    @Override
    public String compactString() {
        String joinedChildren = children.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\":" + entry.getValue().compactString())
                .collect(Collectors.joining(","));
        return "{" + joinedChildren + "}";
    }

    @Override
    public String prettyString(int indentation) {
        String childSpaces = JsonNodeUtils.makeIdentation(indentation + 1);
        String joinedIdentChildren = children.entrySet().stream()
                .map(entry -> childSpaces + "\"" + entry.getKey() + "\" : " + entry.getValue().prettyString(indentation + 1))
                .collect(Collectors.joining(",\n"));
        String spaces = JsonNodeUtils.makeIdentation(indentation);
        return "{\n" +
                joinedIdentChildren +
                "\n" + spaces + "}";
    }

}
