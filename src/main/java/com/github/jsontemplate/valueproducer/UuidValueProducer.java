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

package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonStringNode;
import com.github.jsontemplate.valueproducer.AbstractValueProducer;

import java.util.UUID;

/**
 * This class produces a {@link JsonStringNode} which generates a UUID.
 */
public class UuidValueProducer extends AbstractValueProducer<JsonStringNode> {

    /**
     * The type name used in the template, e.g. {aUuidField: @uuid}
     */
    public static final String TYPE_NAME = "uuid";

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate a random UUID
     *
     * @return the produced json string node
     */
    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceRandomUuid);
    }

    /**
     * Produces a random UUID
     *
     * @return UUID string
     */
    protected String produceRandomUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Produces a node which is based on a UUID-string
     *
     * @return the produced json string node
     */
    @Override
    public JsonStringNode produce(String value) {
        return new JsonStringNode(() -> produceUuid(value));
    }

    /**
     * Produces a UUID based on a UUID-string
     *
     * @return UUID string
     */
    protected String produceUuid(String value) {
        return UUID.fromString(value).toString();
    }
}
