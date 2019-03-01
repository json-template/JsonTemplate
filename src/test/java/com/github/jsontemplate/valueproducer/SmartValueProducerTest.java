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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SmartValueProducerTest {

    private SmartValueProducer producer = new SmartValueProducer();

    @Test
    @DisplayName("generate a value without any input")
    void produce() {
        String result = producer.produce().compactString();
        assertThat(result, is("null"));
    }

    @Test
    @DisplayName("generate a value with null")
    void produceWithNull() {
        String result = producer.produce("null").compactString();
        assertThat(result, is("null"));
    }

    @Test
    @DisplayName("generate a value with an integer string")
    void produceWithIntegerString() {
        String result = producer.produce("123").compactString();
        assertThat(result, is("123"));
    }

    @Test
    @DisplayName("generate a value with a float string")
    void produceWithFloatString() {
        String result = producer.produce("1.23f").compactString();
        assertThat(result, is("1.23"));
    }

    @Test
    @DisplayName("generate a value with a boolean string")
    void produceWithBooleanString() {
        String result = producer.produce("TRUE").compactString();
        assertThat(result, is("true"));
    }

    @Test
    @DisplayName("generate a value with a typical string representation")
    void produceWithAnythingElse() {
        String result = producer.produce("helloworld").compactString();
        assertThat(result, is("\"helloworld\""));
    }

}