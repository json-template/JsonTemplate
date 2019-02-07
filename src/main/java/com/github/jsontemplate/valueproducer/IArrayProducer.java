package com.github.jsontemplate.valueproducer;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface IArrayProducer {
    List produce(Map<String, String> mapParam, Supplier supplier);

    List produce(Map<String, String> mapParam, List<Supplier> valueList);
}
