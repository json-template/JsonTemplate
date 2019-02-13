package com.github.jsontemplate.fluentapi;

import com.github.jsontemplate.jsonbuild.JsonBuilder;
import com.github.jsontemplate.valueproducer.IntegerNodeProducer;
import com.github.jsontemplate.valueproducer.StringNodeProducer;

public class FluentJson {

    private JsonBuilder jsonBuilder = new JsonBuilder();
    private StringNodeProducer stringNodeProducer = new StringNodeProducer();
    private IntegerNodeProducer integerNodeProducer = new IntegerNodeProducer();

    public void build() {
        object(
                string("street").size(5),
                string("name").size(10),
                integer("number").min(10).max(20),
                object("person",
                        string("name"),
                        integer("age").min(0).max(120)
                ),
                array(
                        string(),
                        integer(),
                        object()
                ).defaultValue(string())
        );
    }

    public FluentJsonObjectField object(FluentJsonField... fields) {
        return new FluentJsonObjectField();
    }

    public FluentJsonObjectField object(String name, FluentJsonField... fields) {
        return new FluentJsonObjectField();
    }

    public FluentJsonArrayField array(FluentJsonField... fields) {
        return new FluentJsonArrayField();
    }

    public FluentJsonStringField string(String name, String value) {
        return new FluentJsonStringField();
    }

    public FluentJsonStringField string(String name) {
        return new FluentJsonStringField();
    }

    public FluentJsonStringField string() {
        return new FluentJsonStringField();
    }

    public FluentJsonIntegerField integer(String name, Integer value) {
        return new FluentJsonIntegerField();
    }

    public FluentJsonIntegerField integer(String name) {
        return new FluentJsonIntegerField();
    }

    public FluentJsonIntegerField integer() {
        return new FluentJsonIntegerField();
    }

    public FluentJson object(FluentJson json) {
        if(jsonBuilder.isEmpty()) {
            jsonBuilder.createObject();
        } else {
            jsonBuilder.addObject();
        }
        jsonBuilder.end();
        return this;
    }

    public FluentJson anObject(String name, FluentJson ) {
        jsonBuilder.putObject(name);
        jsonBuilder.end();
        return this;
    }
}
