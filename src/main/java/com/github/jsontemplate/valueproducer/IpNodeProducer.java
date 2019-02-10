package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonStringNode;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class IpNodeProducer extends AbstractNodeProducer<JsonStringNode> {

    private Random random = new Random();

    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceIp);
    }

    private String produceIp() {
        int[] ipParts = new int[]{
                random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255)
        };
        return Arrays.stream(ipParts)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining("."));
    }
}
