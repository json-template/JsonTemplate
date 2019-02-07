package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonNode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractNodeProducer<T extends JsonNode> implements INodeProducer<T> {

    @Override
    public T produce() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T produce(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T produce(List<String> valueList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T produce(Map<String, String> paramMap) {
        throw new UnsupportedOperationException();
    }

    protected Integer pickIntegerParam(Map<String, String> paramMap, String paramName) {
        return pickParamValue(paramMap, paramName, Integer::parseInt);
    }

    protected Integer pickIntegerParam(Map<String, String> paramMap, String paramName, int defaultValue) {
        return defaultIfNull(pickIntegerParam(paramMap, paramName), defaultValue);
    }

    protected Float pickFloatParam(Map<String, String> paramMap, String paramName) {
        return pickParamValue(paramMap, paramName, Float::parseFloat);
    }

    protected Float pickFloatParam(Map<String, String> paramMap, String paramName, float defaultValue) {
        return defaultIfNull(pickFloatParam(paramMap, paramName), defaultValue);
    }

    protected Boolean pickBooleanParam(Map<String, String> paramMap, String paramName) {
        return pickParamValue(paramMap, paramName, Boolean::parseBoolean);
    }

    protected Boolean pickBooleanParam(Map<String, String> paramMap, String paramName, boolean defaultValue) {
        return defaultIfNull(pickBooleanParam(paramMap, paramName), defaultValue);
    }

    protected String pickStringParam(Map<String, String> paramMap, String paramName) {
        return paramMap.get(paramName);
    }

    protected String pickStringParam(Map<String, String> paramMap, String paramName, String defaultValue) {
        return defaultIfNull(pickStringParam(paramMap, paramName), defaultValue);
    }

    protected Type getTypeArgument() {
        return ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected <R> R pickParamValue(Map<String, String> paramMap, String paramName, Function<String, R> parser) {
        String paramValue = paramMap.remove(paramName);
        if (paramValue != null) {
            try {
                return parser.apply(paramValue);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    protected <R> R defaultIfNull(R object, R defaultValue) {
        return object != null ? object : defaultValue;
    }

    protected int randomInRange(int min, int max) {
        int bound = max - min + 1;
        return new Random().nextInt(bound) + min;
    }

    protected void validateParamMap(Map<String, String> paramMap) {
        if (paramMap.size() > 0) {
            String unexpectedArgument = paramMap.keySet().stream().collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Arguments [" + unexpectedArgument + "] is not supported in " + this.getClass().getName());
        }
    }
}
