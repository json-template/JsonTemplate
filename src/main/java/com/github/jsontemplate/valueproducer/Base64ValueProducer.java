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

import java.util.Base64;
import java.util.Map;

/**
 * This class produces a {@link JsonStringNode} which can generate an
 * base64 string.
 */
public class Base64ValueProducer extends AbstractValueProducer<JsonStringNode> {

    /**
     * The type name used in the template, e.g. {aBase64Field: @base64}
     */
    public static final String TYPE_NAME = "base64";
    private static final int DEFAULT_LENGTH = 12;
    private static final int THREE_BYTES = 3;
    private static final int FOUR_BASE64_CHARS = 4;

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate a random base64 string.
     * The length of the string is returned from
     * {@link #getDefaultLength() getDefaultLength()}
     *
     * @return
     */
    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(() -> this.produceBase64(getDefaultLength()));
    }

    /**
     * Produces a node which generate base64 string based on a configuration.
     * <br/>
     * Following parameters are currently supported:
     * <ul>
     * <li>length - the length of the generated base64 string.
     * If it can not be divided by 4, the length of the
     * generated string will be rounded up to the next integer
     * which is multiple of 4.
     * </li>
     * <p>
     * <ul/>
     *
     * @param paramMap configuration
     * @return
     */
    @Override
    public JsonStringNode produce(Map<String, String> paramMap) {
        Integer length = pickIntegerParam(paramMap, "length");

        return new JsonStringNode(() -> this.produceBase64(length));
    }

    /**
     * Generates a base64 string with a given length.
     * If the length can not be divided by 4, the length of the
     * generated string will be rounded up to the next integer
     * which is multiple of 4.
     *
     * @param outputLength expected length of the base64 string
     * @return
     */
    protected String produceBase64(int outputLength) {

        int originalLength = outputLength * THREE_BYTES / FOUR_BASE64_CHARS;
        if (outputLength % FOUR_BASE64_CHARS != 0) {
            originalLength += 1;
        }
        String originalString = new StringValueProducer().produceString(originalLength);
        return Base64.getEncoder().encodeToString(originalString.getBytes());
    }

    /**
     * Returns the default length of the base64 string
     *
     * @return
     */
    protected int getDefaultLength() {
        return DEFAULT_LENGTH;
    }
}
