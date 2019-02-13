package com.github.jsontemplate.fluentapi;

public class FluentJsonIntegerField implements FluentJsonField {

    public FluentJsonIntegerField value(String value) {
        return this;
    }

    public FluentJsonIntegerField enumeration(Integer... enumeration) {
        return this;
    }

    public FluentJsonIntegerField max(int max) {
        return this;
    }

    public FluentJsonIntegerField min(int min) {
        return this;
    }
}
