package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonNode;

import java.util.List;
import java.util.Map;

public interface INodeProducer<T extends JsonNode> {

    T produce();

    T produce(String value);

    T produce(List<String> valueList);

    T produce(Map<String, String> paramMap);
}
