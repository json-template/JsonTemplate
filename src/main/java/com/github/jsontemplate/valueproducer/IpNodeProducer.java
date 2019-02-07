package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonStringNode;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class IpNodeProducer extends AbstractNodeProducer<JsonStringNode> {

    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceIp);
    }

    private String produceIp() {
        Random random = new Random();
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
