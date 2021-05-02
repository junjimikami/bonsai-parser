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
import java.util.function.Supplier;

import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.SequentialProduction;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 *
 * @author Junji Mikami
 */
class SeqProduction extends AbstractProduction implements SequentialProduction {
    private final List<AbstractProduction> elements;

    SeqProduction(List<AbstractProduction> elements) {
        this.elements = Objects.requireNonNull(elements);
    }

    @Override
    Symbol interpret(Tokenizer.Buffer buffer, Set<TermProduction> followSet) {
        Set<TermProduction> firstSet = getFirstSet(followSet);
        if (anyMatch(firstSet, buffer)) {
            List<Symbol> list = new ArrayList<>();
            for (int i = 0; i < elements.size(); i++) {
                list.add(elements.get(i).interpret(buffer, getFollowSet(i, followSet)));
            }
            return newNonTerminal(this, list);
        }
        throw newException(Message.RULE_MISMATCH, firstSet, buffer);
    }

    @Override
    Set<TermProduction> getFirstSet(Set<TermProduction> followSet) {
        if (elements.isEmpty()) {
            return followSet;
        }
        return getFirstSet(0, followSet);
    }

    private Set<TermProduction> getFirstSet(int i, Set<TermProduction> followSet) {
        assert 0 <= i && i < elements.size();
        Set<TermProduction> set = new HashSet<>();
        set.addAll(elements.get(i).getFirstSet());
        if (elements.get(i).isOption()) {
            set.addAll(getFollowSet(i, followSet));
        }
        return set;
    }

    private Set<TermProduction> getFollowSet(int i, Set<TermProduction> followSet) {
        assert 0 <= i && i < elements.size();
        if (elements.size() - 1 == i) {
            return followSet;
        }
        return getFirstSet(i + 1, followSet);
    }

    @Override
    boolean isOption() {
        if (elements.isEmpty()) {
            return true;
        }
        return elements.stream().allMatch(p -> p.isOption());
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    static class Builder implements SequentialProduction.Builder {
        private final List<AbstractProduction> elements = new ArrayList<>();
        private boolean isBuilt;
        
        @Override
        public SequentialProduction build() {
            if (isBuilt) {
                throw new IllegalStateException();
            }
            isBuilt = true;
            return new SeqProduction(elements);
        }
        
        @Override
        public Builder add(Production p) {
            addIfBuilding((AbstractProduction) p);
            return this;
        }
        
        @Override
        public Builder add(String s) {
            addIfBuilding(new TermProduction(s));
            return this;
        }
        
        @Override
        public Builder add(Supplier<? extends Production> p) {
            addIfBuilding(new RefProduction(p));
            return this;
        }

        private void addIfBuilding(AbstractProduction e) {
            if (isBuilt) {
                throw new IllegalStateException();
            }
            elements.add(e);
        }
    }

}
