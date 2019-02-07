package com.github.jsontemplate.jsonbuild;

import java.util.function.Supplier;

public abstract class AbstractJsonValueNode<T> implements JsonValueNode {

    protected Supplier<T> supplier;

    public AbstractJsonValueNode(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public String prettyPrint(int identation) {
        return print();
    }
}
