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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.unitedjiga.common.parsing.SequentialProduction;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 * 
 * @author Junji Mikami
 *
 */
class RepeatProduction extends AbstractProduction implements SequentialProduction {
    private final AbstractProduction element;

    RepeatProduction(AbstractProduction p) {
        element = Objects.requireNonNull(p);
    }

    @Override
    Symbol interpret(Tokenizer.Buffer buffer, Set<TermProduction> followSet) {
        List<Symbol> list = new ArrayList<>();
        while (anyMatch(getFirstSet(), buffer)) {
            list.add(element.interpret(buffer, getFollowSet(followSet)));
        }
        return newNonTerminal(this, list);
    }

    @Override
    Set<TermProduction> getFirstSet(Set<TermProduction> followSet) {
        return element.getFirstSet(followSet);
    }

    private Set<TermProduction> getFollowSet(Set<TermProduction> followSet) {
        Set<TermProduction> set = new HashSet<>();
        set.addAll(element.getFirstSet());
        set.addAll(followSet);
        return set;
    }

    @Override
    boolean isOption() {
        return true;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}
