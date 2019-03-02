/*
 * Copyright 2019 Haihan Yin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jsontemplate.jsonbuild;

import com.github.jsontemplate.valueproducer.IValueProducer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a producer of a json array value.
 */
public final class JsonArrayNode implements JsonNode {

    private List<JsonNode> children = new LinkedList<>();
    private JsonNode defaultNode;
    private Integer size;
    private Integer max;
    private Integer min;

    /**
     * Creates a JsonArrayNode with a given collection.
     *
     * @param collection
     * @return
     */
    public static JsonArrayNode of(Collection<?> collection) {
        JsonArrayNode jsonArrayNode = new JsonArrayNode();
        collection.stream()
                .map(JsonNode::of)
                .forEach(jsonArrayNode::addNode);
        return jsonArrayNode;
    }

    /**
     * Creates a JsonArrayNode with a given array.
     *
     * @param objects
     * @return
     */
    public static JsonArrayNode of(Object[] objects) {
        JsonArrayNode jsonArrayNode = new JsonArrayNode();
        Arrays.stream(objects)
                .map(JsonNode::of)
                .forEach(jsonArrayNode::addNode);
        return jsonArrayNode;
    }

    /**
     * Add a JsonNode as its elements.
     *
     * @param jsonNode
     */
    public void addNode(JsonNode jsonNode) {
        children.add(jsonNode);
    }

    /**
     * Sets the default node.
     *
     * @param jsonNode
     */
    public void setDefaultNode(JsonNode jsonNode) {
        this.defaultNode = jsonNode;
    }

    /**
     * Sets the single parameter. This parameter is for the array
     * instead of the elements.
     * <p/>
     * By default, the single parameter is interpreted as the size
     * specification of the array.
     * <p/>
     * For example, template <code>@s[](5)</code> is a short hand
     * for <code>@s[](size=5)</code>.
     *
     * @param singleParam
     * @see #setParameters(Map)
     */
    public void setParameters(String singleParam) {
        size = Integer.parseInt(singleParam);
    }

    /**
     * Sets the list parameter. This parameter is for the array
     * instead of the elements.
     * <p/>
     * By default, the list parameter is interpreted as the size range
     * specification of the array.
     * <p/>
     * For example, template <code>@s[](2, 5)</code> is a short hand
     * for <code>@s[](min=2, max=5)</code>.
     *
     * @param singleParam
     * @see #setParameters(Map)
     */
    public void setParameters(List<String> singleParam) {
        min = Integer.parseInt(singleParam.get(0));
        max = Integer.parseInt(singleParam.get(1));
    }

    /**
     * Sets the map parameter. This parameter is for the array
     * instead of the elements.
     * <p/>
     * Currently, only size, min, max are supported.
     * <p/>
     * If the template has already specified the contained elements,
     * The result size range will always first satisfy the elements,
     * then the size specification, and at last the min and max specification.
     * For example,
     * <ul>
     * <li>
     * "@s[A, B, C](size=2)", the result size is 3, the result
     * array will contain "A", "B", "C", and none random strings.
     * </li>
     * * <li>
     * "@s[A, B, C](size=4)", the result size is 4, the result
     * array will contain "A", "B", "C", and 1 random strings.
     * </li>
     * <li>
     * "@s[A, B, C](min=4, max=6)", the result size range is (4, 6), the result
     * array will contain "A", "B", "C", and 1 to 3 random strings.
     * </li>
     * <li>
     * "@s[A, B, C, D, E](min=4, max=6)", the result size range is (5, 6), the result
     * array will contain "A", "B", "C", "D", "E" and 0 to 1 random strings.
     * </li>
     *
     * <li>
     * "@s[A, B, C, D, E, F, G](min=4, max=6)", the result size is 7, the result
     * array will contain "A", "B", "C", "D", "E" , "F", "G", and none random strings.
     * </li>
     * </ul>
     *
     * @param mapParam
     */
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

    @Override
    public String compactString() {
        String joinedChildren = children.stream().map(JsonNode::compactString).collect(Collectors.joining(","));
        return "[" + joinedChildren + "]";
    }

    @Override
    public String prettyString(int identation) {
        String childrenSpaces = JsonNodeUtils.makeIdentation(identation + 1);
        ArrayList<JsonNode> printChildren = new ArrayList<>();

        printChildren.addAll(children);
        List<JsonNode> additionalNodeList = prepareAdditionalNodeList();
        printChildren.addAll(additionalNodeList);

        String joinedIdentChildren = printChildren.stream()
                .map(child -> childrenSpaces + child.prettyString(identation + 1))
                .collect(Collectors.joining(",\n"));

        String spaces = JsonNodeUtils.makeIdentation(identation);
        return "[\n" +
                joinedIdentChildren +
                "\n" + spaces + "]";
    }

}
