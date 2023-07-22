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

package com.github.jsontemplate.jsonbuild.supplier;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * The ListParamSupplier class is an implementation of the Supplier interface that supplies elements from a given list.
 * It allows you to provide a list of elements of type T and retrieve a random element from the list whenever the 'get'
 * method is called on the supplier.
 *
 * @param <T> The type of elements held in the list and returned by the supplier.
 */
public record ListParamSupplier<T>(List<T> listParam) implements Supplier<T> {

    /**
     * Constructs a new ListParamSupplier with the specified list of elements.
     *
     * @param listParam The list containing elements of type T to be supplied by the supplier.
     */
    public ListParamSupplier {
    }

    /**
     * Gets the list of elements that the supplier provides.
     *
     * @return The list of elements that the supplier supplies.
     */
    @Override
    public List<T> listParam() {
        return listParam;
    }

    /**
     * Supplies a random element from the list. The 'get' method returns an element chosen randomly from the list.
     *
     * @return A random element of type T from the list.
     * @throws IndexOutOfBoundsException If the list is empty and 'get' is called.
     */
    @Override
    public T get() {
        return listParam.get(new Random().nextInt(listParam.size()));
    }
}
