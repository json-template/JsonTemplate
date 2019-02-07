package com.github.jsontemplate.jsonbuild;

import java.util.function.Supplier;

public class JsonStringNode extends AbstractJsonValueNode<String> {

    public static JsonStringNode of(String value) {
        return new JsonStringNode(() -> value);
    }

    public JsonStringNode(Supplier<String> supplier) {
        super(supplier);
    }

    @Override
    public String print() {
        return "\"" + supplier.get() + "\"";
    }

}
