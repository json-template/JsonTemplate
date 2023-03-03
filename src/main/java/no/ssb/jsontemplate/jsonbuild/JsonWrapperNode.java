package no.ssb.jsontemplate.jsonbuild;

/**
 * This class is a wrapper of another JsonNode. It is used
 * as a placeholder of an unknown or future-created JsonNode.
 */
public final class JsonWrapperNode implements JsonNode {

    private JsonNode jsonNode;

    public void setJsonNode(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    @Override
    public String compactString() {
        return jsonNode.compactString();
    }

    @Override
    public String prettyString(int indentation) {
        return jsonNode.prettyString(indentation);
    }
}
