package com.github.jsontemplate.fluentapi;

public class FluentJsonStringField implements FluentJsonField {

    public FluentJsonStringField value(String value) {
        return this;
    }

    public FluentJsonStringField enumeration(String... enumeration) {
        return this;
    }

    public FluentJsonStringField size(int size) {
        return this;
    }

    public FluentJsonStringField max(int max) {
        return this;
    }

    public FluentJsonStringField min(int min) {
        return this;
    }
}
