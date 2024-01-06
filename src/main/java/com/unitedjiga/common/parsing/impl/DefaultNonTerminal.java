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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.unitedjiga.common.parsing.NonTerminal;
import com.unitedjiga.common.parsing.Tree;

/**
 * 
 * @author Junji Mikami
 *
 */
class DefaultNonTerminal implements NonTerminal {
    private final String symbol;
    private final List<? extends Tree> list;

    DefaultNonTerminal(String symbol, List<? extends Tree> list) {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(list);
        this.symbol = symbol;
        this.list = list;
    }

    DefaultNonTerminal(String symbol, Tree s) {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(s);
        this.symbol = symbol;
        this.list = List.of(s);
    }

    DefaultNonTerminal(String symbol) {
        this(symbol, List.of());
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public List<? extends Tree> getSubTrees() {
        return Collections.unmodifiableList(list);
    }
}
