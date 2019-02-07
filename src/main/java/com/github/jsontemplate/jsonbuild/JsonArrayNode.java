package com.github.jsontemplate.jsonbuild;

import com.github.jsontemplate.valueproducer.INodeProducer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class JsonArrayNode implements JsonNode {

    public static JsonArrayNode of(Collection<?> collection) {
        JsonArrayNode jsonArrayNode = new JsonArrayNode();
        collection.stream()
                .map(JsonNode::of)
                .forEach(jsonArrayNode::addNode);
        return jsonArrayNode;
    }

    public static JsonArrayNode of(Object[] objects) {
        JsonArrayNode jsonArrayNode = new JsonArrayNode();
        Arrays.stream(objects)
                .map(JsonNode::of)
                .forEach(jsonArrayNode::addNode);
        return jsonArrayNode;
    }


    private List<JsonNode> children = new LinkedList<>();

    private JsonNode defaultNode;
    private INodeProducer defaultNodeProducer;
    private Integer size;
    private Integer max;
    private Integer min;

    public void addNode(JsonNode jsonNode) {
        children.add(jsonNode);
    }

    public void setNodeProducer(INodeProducer producer) {
        this.defaultNodeProducer = producer;
    }

    public INodeProducer getDefaultType() {
        return defaultNodeProducer;
    }

    public void setDefaultNode(JsonNode jsonNode) {
        this.defaultNode = jsonNode;
    }

    public void setParameters(String singleParam) {
        size = Integer.parseInt(singleParam);
    }

    public void setParameters(List<String> singleParam) {
        min = Integer.parseInt(singleParam.get(0));
        max = Integer.parseInt(singleParam.get(1));
    }

    public void setParameters(Map<String, String> mapParam) {
        size = readParam(mapParam, "size");
        max = readParam(mapParam, "max");
        min = readParam(mapParam, "min");

        if (size == null) {
            if (min != null && max == null) {
                max = 2 * min;
            } else if (min == null && max != null) {
                min = 0;
            }
        }
    }

    private List<JsonNode> prepareAdditionalNodeList() {
        if (size != null) {
            return addtionalNodeList(size);
        } else if (max != null && children.size() < max) {
            int randomSize = new Random().nextInt(max - min) + min;
            return addtionalNodeList(randomSize);
        } else {
            return Collections.emptyList();
        }
    }

    private List<JsonNode> addtionalNodeList(int size) {
        if (size > children.size()) {
            int amount = size - children.size();
            List<JsonNode> list = new ArrayList<>(amount);
            if (defaultNode != null) {
                for (int i = 0; i < amount; i++) {
                    list.add(defaultNode);
                }
            } else if (defaultNodeProducer != null) {
                for (int i = 0; i < amount; i++) {
                    list.add(defaultNodeProducer.produce());
                }
            }
            return list;
        } else {
            return Collections.emptyList();
        }
    }

    private Integer readParam(Map<String, String> mapParam, String key) {
        String value = mapParam.get(key);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return null;
    }

    public void addObject(JsonObjectNode value) {
        children.add(value);
    }

    public void addArray(JsonArrayNode value) {
        children.add(value);
    }

    @Override
    public String print() {
        String joinedChildren = children.stream().map(JsonNode::print).collect(Collectors.joining(","));
        return "[" + joinedChildren + "]";
    }

    @Override
    public String prettyPrint(int identation) {
        String childrenSpaces = JsonNodeUtils.makeIdentation(identation + 1);
        ArrayList<JsonNode> printChildren = new ArrayList<>();

        printChildren.addAll(children);
        List<JsonNode> additionalNodeList = prepareAdditionalNodeList();
        printChildren.addAll(additionalNodeList);

        String joinedIdentChildren = printChildren.stream()
                .map(child -> childrenSpaces + child.prettyPrint(identation + 1))
                .collect(Collectors.joining(",\n"));

        String spaces = JsonNodeUtils.makeIdentation(identation);
        return "[\n" +
                joinedIdentChildren +
                "\n" + spaces + "]";
    }

}
