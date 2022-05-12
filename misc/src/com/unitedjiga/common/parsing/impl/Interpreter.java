/*
 * The MIT License
 *
 * Copyright 2022 Mikami Junji.
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
import java.util.LinkedList;
import java.util.Set;

import com.unitedjiga.common.parsing.AlternativeProduction;
import com.unitedjiga.common.parsing.ParsingException;
import com.unitedjiga.common.parsing.PatternProduction;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.ProductionVisitor;
import com.unitedjiga.common.parsing.QuantifiedProduction;
import com.unitedjiga.common.parsing.Reference;
import com.unitedjiga.common.parsing.SequentialProduction;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.TerminalProduction;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 * @author Mikami Junji
 *
 */
class Interpreter implements ProductionVisitor<Symbol, Object[]> {
    private final FirstSet firstSet = new FirstSet();

    Symbol interpret(Production prd, Tokenizer t) {
        var followSet = Set.<TerminalProduction>of();
        return interpret(prd, t, followSet);
    }
    Symbol interpret(Production prd, Tokenizer tokenizer, Set<TerminalProduction> followSet) {
        var p = new Object[] {
                tokenizer,
                followSet
        };
        return ProductionVisitor.super.visit(prd, p);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Symbol visitAlternative(AlternativeProduction alt, Object[] args) {
        var tokenizer = (Tokenizer) args[0];
        var followSet = (Set<TerminalProduction>) args[1];
        for (var p : alt.getProductions()) {
            if (firstSet.anyMatch(p, tokenizer, followSet)) {
                var s = interpret(p, tokenizer, followSet);
                return new AbstractNonTerminalSymbol(alt.getName(), s);
            }
        }
        throw new ParsingException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Symbol visitSequential(SequentialProduction seq, Object[] args) {
        var tokenizer = (Tokenizer) args[0];
        var followSet = (Set<TerminalProduction>) args[1];
        if (firstSet.anyMatch(seq, tokenizer, followSet)) {
            var list = new ArrayList<Symbol>();
            var pList = new LinkedList<>(seq.getProductions());
            while (!pList.isEmpty()) {
                var p = pList.remove();
                var f = firstSet.visitSequential(pList, followSet);
                list.add(interpret(p, tokenizer, f));
            }
            return new AbstractNonTerminalSymbol(seq.getName(), list);
        }
        throw new ParsingException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Symbol visitPattern(PatternProduction p, Object[] args) {
        var tokenizer = (Tokenizer) args[0];
        var followSet = (Set<TerminalProduction>) args[1];
        if (firstSet.anyMatch(p, tokenizer, followSet)) {
            var t = tokenizer.remove();
            return new DefaultTerminalSymbol(p.getName(), t.getValue());
        }
        throw new ParsingException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Symbol visitReference(Reference<?> ref, Object[] args) {
        var tokenizer = (Tokenizer) args[0];
        var followSet = (Set<TerminalProduction>) args[1];
        var s = interpret(ref.get(), tokenizer, followSet);
        return new AbstractNonTerminalSymbol(ref.getName(), s);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Symbol visitQuantified(QuantifiedProduction qt, Object[] args) {
        var tokenizer = (Tokenizer) args[0];
        var followSet = (Set<TerminalProduction>) args[1];
        var list = new ArrayList<Symbol>();
        var it = qt.stream().iterator();
        while (it.hasNext()) {
            var p = it.next();
            if (!firstSet.anyMatch(p, tokenizer, followSet)) {
                break;
            }
            list.add(interpret(p, tokenizer, followSet));
        }
        if (qt.getLowerLimit() <= list.size()) {
            return new AbstractNonTerminalSymbol(qt.getName(), list);
        }
        throw new ParsingException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Symbol visitEmpty(TerminalProduction empty, Object[] args) {
        var tokenizer = (Tokenizer) args[0];
        var followSet = (Set<TerminalProduction>) args[1];
        if (firstSet.anyMatch(empty, tokenizer, followSet)) {
            return new DefaultTerminalSymbol(empty.getName());
        }
        throw new ParsingException();
    }
}
