/*
 * Copyright 2019 Haihan Yin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonStringNode;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class produces a {@link JsonStringNode} which can generate an
 * ipv6 string.
 */
public class IpValueProducer extends AbstractValueProducer<JsonStringNode> {

    /**
     * The type name used in the template, e.g. {anIpField: @ip}
     */
    public static final String TYPE_NAME = "ip";

    private Random random = new Random();

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate a random ip string
     *
     * @return
     */
    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceIp);
    }

    /**
     * Produces an ip string
     *
     * @return
     */
    protected String produceIp() {
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
