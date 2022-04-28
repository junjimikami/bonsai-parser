/*
 * The MIT License
 *
 * Copyright 2021 Junji Mikami.
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

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
class RefProduction extends AbstractProduction {
    private final Supplier<? extends Production> p;

    RefProduction(Supplier<? extends Production> p) {
        this.p = Objects.requireNonNull(p, Message.REQUIRE_NON_NULL.format());
    }

    @Override
    Set<TermProduction> getFirstSet(Set<TermProduction> followSet) {
        return get().getFirstSet(followSet);
    }

    @Override
    Symbol interpret(Tokenizer tokenizer, Set<TermProduction> followSet) {
        return get().interpret(tokenizer, followSet);
    }

    @Override
    boolean isOption() {
        return get().isOption();
    }

    private AbstractProduction get() {
        return (AbstractProduction) p.get();
    }
}