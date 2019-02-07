package com.github.jsontemplate.jsonbuild;

public class JsonWrapperNode implements JsonNode {

    private JsonNode jsonNode;

    public JsonNode getJsonNode() {
        return jsonNode;
    }

    public void setJsonNode(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    @Override
    public String print() {
        return jsonNode.print();
    }

    @Override
    public String prettyPrint(int identation) {
        return jsonNode.prettyPrint(identation);
    }
}
