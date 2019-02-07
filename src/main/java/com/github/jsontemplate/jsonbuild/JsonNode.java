package com.github.jsontemplate.jsonbuild;

import java.util.Collection;
import java.util.Map;

public interface JsonNode {

    String print();

    String prettyPrint(int identation);

    public static JsonNode of(Object obj) {
        JsonNode jsonNode;
        if (obj == null) {
            jsonNode = new JsonNullNode();
        } else if (obj instanceof Integer) {
            jsonNode = JsonIntegerNode.of((Integer) obj);
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
}
