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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.unitedjiga.common.parsing.ParsingException;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 *
 * @author Junji Mikami
 */
class AndProduction extends AbstractProduction {
    private final List<AbstractProduction> elements = new ArrayList<>();
    private final Pattern pattern;

    AndProduction(CharSequence... production) {
        for (CharSequence cs : production) {
            elements.add(cs instanceof AbstractProduction ? (AbstractProduction) cs : new TermProduction(cs));
        }
        pattern = elements.stream().map(e -> "(" + e + ")")
                .collect(Collectors.collectingAndThen(Collectors.joining(), Pattern::compile));
    }

    @Override
    Symbol interpret(Tokenizer tokenizer, Set<TermProduction> followSet) {
        if (anyMatch(getFirstSet(followSet), tokenizer)) {
            List<Symbol> list = new ArrayList<>();
            for (int i = 0; i < elements.size(); i++) {
                list.add(elements.get(i).interpret(tokenizer, getFollowSet(i, followSet)));
            }
            return newNonTerminal(this, list);
        }
        Object[] args = { getFirstSet(followSet), tryNext(tokenizer) };
        throw new ParsingException(Messages.RULE_MISMATCH.format(args));
    }

    @Override
    Set<TermProduction> getFirstSet(Set<TermProduction> followSet) {
        if (elements.isEmpty()) {
            return followSet;
        }
        Set<TermProduction> set = new HashSet<>();
        set.addAll(elements.get(0).getFirstSet());
        set.addAll(getFollowSet(0, followSet));
        return set;
    }

    private Set<TermProduction> getFollowSet(int i, Set<TermProduction> followSet) {
        assert 0 <= i && i < elements.size();
        if (!elements.get(i).isOption()) {
            return Collections.emptySet();
        }
        if (elements.size() - 1 == i) {
            return followSet;
        }
        Set<TermProduction> set = new HashSet<>();
        set.addAll(elements.get(i + 1).getFirstSet());
        set.addAll(getFollowSet(i + 1, followSet));
        return set;
    }

    @Override
    boolean isOption() {
        if (elements.isEmpty()) {
            return true;
        }
        return elements.stream().allMatch(p -> p.isOption());
    }

    @Override
    public Pattern asPattern() {
        return pattern;
    }

}
