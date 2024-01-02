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

import static com.unitedjiga.common.parsing.impl.Productions.EOF;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import com.unitedjiga.common.parsing.ChoiceExpression;
import com.unitedjiga.common.parsing.ParsingException;
import com.unitedjiga.common.parsing.PatternExpression;
import com.unitedjiga.common.parsing.Expression;
import com.unitedjiga.common.parsing.ExpressionVisitor;
import com.unitedjiga.common.parsing.QuantifierExpression;
import com.unitedjiga.common.parsing.ReferenceExpression;
import com.unitedjiga.common.parsing.SequenceExpression;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 * @author Mikami Junji
 *
 */
class Interpreter implements ExpressionVisitor<Symbol, Context> {
    private final FirstSet firstSet = new FirstSet();
    private final AnyMatcher anyMatcher = new AnyMatcher();

    Symbol parse(Expression prd, Tokenizer t) {
        var s = interpret(prd, t);
        if (t.hasNext()) {
            throw new ParsingException();
        }
        return s;
    }
    Symbol interpret(Expression prd, Tokenizer t) {
        var followSet = Set.of(EOF);
        return interpret(prd, t, followSet);
    }
    Symbol interpret(Expression prd, Tokenizer tokenizer, Set<Expression> followSet) {
        var p = new Context(tokenizer, followSet);
        return visit(prd, p);
    }

    @Override
    public Symbol visitChoice(ChoiceExpression alt, Context args) {
        for (var p : alt.getChoices()) {
            if (anyMatcher.visit(p, args)) {
                var s = visit(p, args);
                return new DefaultNonTerminalSymbol(alt.getName(), s);
            }
        }
        throw new ParsingException();
    }

    @Override
    public Symbol visitSequence(SequenceExpression seq, Context args) {
        var tokenizer = args.getTokenizer();
        var followSet = args.getFollowSet();
        if (anyMatcher.visit(seq, args)) {
            var list = new ArrayList<Symbol>();
            var pList = new LinkedList<>(seq.getSequence());
            while (!pList.isEmpty()) {
                var p = pList.remove();
                var subseq = Productions.of(pList.toArray());
                var f = firstSet.visitSequence(subseq, followSet);
                list.add(interpret(p, tokenizer, f));
            }
            return new DefaultNonTerminalSymbol(seq.getName(), list);
        }
        throw new ParsingException();
    }

    @Override
    public Symbol visitPattern(PatternExpression p, Context args) {
        var tokenizer = args.getTokenizer();
        if (anyMatcher.visit(p, args)) {
            var t = tokenizer.read();
            return new DefaultNonTerminalSymbol(p.getName(), t);
        }
        throw new ParsingException();
    }

    @Override
    public Symbol visitReference(ReferenceExpression ref, Context args) {
        return visit(ref.get(), args);
    }

    @Override
    public Symbol visitQuantifier(QuantifierExpression qt, Context args) {
        var list = new ArrayList<Symbol>();
        var result = qt.withinRange(p -> {
            if (anyMatcher.visit(p, args)) {
                list.add(visit(p, args));
                return true;
            }
            return false;
        });
        if (result) {
            return new DefaultNonTerminalSymbol(qt.getName(), list);
        }
        throw new ParsingException();
    }

    @Override
    public Symbol visitEmpty(Expression empty, Context args) {
        if (anyMatcher.visit(empty, args)) {
            return new DefaultNonTerminalSymbol(null);
        }
        throw new ParsingException();
    }
}
