package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonStringNode;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ipv6NodeProducer extends AbstractNodeProducer<JsonStringNode> {

    private static final String LETTERS = "0123456789abcdef";

    private Random random = new Random();

    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceIp);
    }

    protected String produceIp() {
        return IntStream.range(0, 8)
                .mapToObj(i -> produceGroup())
                .collect(Collectors.joining(":"));
    }

    private String produceGroup() {
        int length = LETTERS.length();
        char[] group = new char[]{
                LETTERS.charAt(random.nextInt(length)),
                LETTERS.charAt(random.nextInt(length)),
                LETTERS.charAt(random.nextInt(length)),
                LETTERS.charAt(random.nextInt(length))
        };
        return new String(group);
    }
}
