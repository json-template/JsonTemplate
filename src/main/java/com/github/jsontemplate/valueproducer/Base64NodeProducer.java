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

public class Base64NodeProducer extends AbstractNodeProducer<JsonStringNode> {

    public static final int DEFAULT_LENGTH = 12;

    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(() -> this.produceBase64(getDefaultLength()));
    }

    @Override
    public JsonStringNode produce(Map<String, String> paramMap) {
        Integer length = pickIntegerParam(paramMap, "length");

        return new JsonStringNode(() -> this.produceBase64(length));
    }

    private String produceBase64(int outputLength) {
        int originalLength = outputLength * 3 / 4;
        if (outputLength % 4 != 0) {
            originalLength += 1;
        }
        String originalString = new StringNodeProducer().produceString(originalLength);
        return Base64.getEncoder().encodeToString(originalString.getBytes());
    }

    protected int getDefaultLength() {
        return DEFAULT_LENGTH;
    }
}
