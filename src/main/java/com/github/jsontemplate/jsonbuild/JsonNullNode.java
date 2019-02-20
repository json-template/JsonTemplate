package com.github.jsontemplate.jsonbuild;

public final class JsonNullNode implements JsonValueNode {

    @Override
    public String print() {
        return "null";
    }

    @Override
    public String prettyPrint(int identation) {
        return print();
    }
}
