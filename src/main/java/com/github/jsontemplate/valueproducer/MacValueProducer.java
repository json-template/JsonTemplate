package com.github.jsontemplate.valueproducer;

  
/*
 * Copyright 2019 the original author or authors.
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



import com.github.jsontemplate.jsonbuild.JsonStringNode;
import com.github.jsontemplate.valueproducer.AbstractValueProducer;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class produces a {@link JsonStringNode} which can generate a
 * 6-octet MAC address string. 
 */
public class MacValueProducer extends AbstractValueProducer<JsonStringNode> {

    /**
     * The type name used in the template, e.g. {aMacField: @mac}
     */
    public static final String TYPE_NAME = "mac";

    private final Random random = new Random();

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate a random ip string
     *
     * @return the produced json string node
     */
    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceMac);
    }

    /**
     * Produces an ip string
     *
     * @return ip format string
     */
    protected String produceMac() {
        String[] macParts = new String[]{
        		String.format("%02X", (random.nextInt(255) & 0xF0)), //& 0xF0 to keep it a universal mac address
        		String.format("%02X", random.nextInt(255)),
        		String.format("%02X", random.nextInt(255)),
        		String.format("%02X", random.nextInt(255)),
        		String.format("%02X", random.nextInt(255)),
        		String.format("%02X", random.nextInt(255))
        };
        return String.join(":", macParts);
    }
}

