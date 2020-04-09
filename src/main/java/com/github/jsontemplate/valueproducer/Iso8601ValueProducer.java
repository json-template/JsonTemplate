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

import java.time.Instant;

/**
 * This class produces a {@link JsonStringNode} which generates current ISO8601 timestamp. 
 */
public class Iso8601ValueProducer extends AbstractValueProducer<JsonStringNode> {

    /**
     * The type name used in the template, e.g. {anIsoField: @iso8601}
     */
    public static final String TYPE_NAME = "iso8601";

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate current ISO8601 timestamp
     *
     * @return the produced json string node
     */
    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceTs);
    }

    /**
     * Produces an ISO8601 timestamp based on current time
     *
     * @return ISO8601 timestamp string
     */
    protected String produceTs() {
        return Instant.now().toString();
    }
}

