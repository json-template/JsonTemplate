package com.github.jsontemplate.jsonbuild;

import java.util.function.Supplier;

abstract class AbstractJsonValueNode<T> implements JsonValueNode {

    protected Supplier<T> supplier;

    AbstractJsonValueNode(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public final String prettyPrint(int identation) {
        return print();
    }
}
