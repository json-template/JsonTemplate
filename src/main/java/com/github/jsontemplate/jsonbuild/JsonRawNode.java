package com.github.jsontemplate.jsonbuild;

import java.util.function.Supplier;

public final class JsonRawNode extends AbstractJsonValueNode<String> {

    public JsonRawNode(Supplier<String> supplier) {
        super(supplier);
    }

    @Override
    public String print() {
        return supplier.get();
    }
}
