/*
 * The MIT License
 *
 * Copyright 2020 Junji Mikami.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.unitedjiga.common.parsing.impl;

import java.util.function.Supplier;

import com.unitedjiga.common.parsing.AlternativeProduction;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.SequentialProduction;

/**
 * 
 * @author Junji Mikami
 *
 */
public final class Productions {

    private Productions() {
    }

    public static SequentialProduction.Builder sequentialBuilder() {
        return new SeqProduction.Builder();
    }
    
    public static AlternativeProduction.Builder alternativeBuilder() {
        return new AltProduction.Builder();
    }

    @SuppressWarnings("unchecked")
    public static SequentialProduction of(Object... args) {
        SequentialProduction.Builder builder = sequentialBuilder();
        for (Object o : args) {
            if (o instanceof Production) {
                builder.add((Production) o);
            } else if (o instanceof String) {
                builder.add((String) o);
            } else if (o instanceof Supplier) {
                builder.add((Supplier<? extends Production>) o);
            } else {
                builder.add(o.toString());
            }
        }
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    public static AlternativeProduction oneOf(Object... args) {
        AlternativeProduction.Builder builder = alternativeBuilder();
        for (Object o : args) {
            if (o instanceof Production) {
                builder.add((Production) o);
            } else if (o instanceof String) {
                builder.add((String) o);
            } else if (o instanceof Supplier) {
                builder.add((Supplier<? extends Production>) o);
            } else {
                builder.add(o.toString());
            }
        }
        return builder.build();
    }

    public static Production empty() {
        return sequentialBuilder().build();
    }
}
