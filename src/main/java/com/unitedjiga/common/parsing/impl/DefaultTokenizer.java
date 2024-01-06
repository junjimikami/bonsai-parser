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

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.unitedjiga.common.parsing.NonTerminal;
import com.unitedjiga.common.parsing.Terminal;
import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.TreeVisitor;
import com.unitedjiga.common.parsing.grammar.Grammar;
import com.unitedjiga.common.parsing.grammar.Production;

/**
 * @author Junji Mikami
 *
 */
class DefaultTokenizer implements Tokenizer {

    private final TreeVisitor<String, Void> treeToString = new TreeVisitor<>() {
        @Override
        public String visitNonTerminal(NonTerminal s, Void p) {
            return s.getSubTrees().stream()
                    .map(this::visit)
                    .collect(Collectors.joining());
        }

        @Override
        public String visitTerminal(Terminal s, Void p) {
            return s.getValue();
        }
    };

    private final Production production;
    private final Context context;
    private String nextToken;

    DefaultTokenizer(Grammar grammar, Tokenizer tokenizer) {
        Objects.requireNonNull(grammar);
        Objects.requireNonNull(tokenizer);
        this.production = grammar.getStart();
        this.context = new Context(tokenizer, Set.of());
    }

    private String read() {
        if (nextToken != null) {
            return nextToken;
        }
        if (!context.getTokenizer().hasNext()) {
            return null;
        }
        nextToken = Interpreter.parse(production, context)
                .accept(treeToString);
        return nextToken;
    }

    @Override
    public boolean hasNext() {
        return read() != null;
    }

    @Override
    public boolean hasNext(String regex) {
        Objects.requireNonNull(regex);
        var pattern = Pattern.compile(regex);
        return hasNext(pattern);
    }

    @Override
    public boolean hasNext(Pattern pattern) {
        Objects.requireNonNull(pattern);
        if (!hasNext()) {
            return false;
        }
        var matcher = pattern.matcher(nextToken);
        return matcher.lookingAt();
    }

    @Override
    public Token next() {
        var value = read();
        if (value == null) {
            throw new NoSuchElementException(Message.NO_SUCH_TOKEN.format());
        }
        nextToken = null;
        return new DefaultToken(value);
    }

    @Override
    public Token next(String regex) {
        Objects.requireNonNull(regex);
        var pattern = Pattern.compile(regex);
        return next(pattern);
    }

    @Override
    public Token next(Pattern pattern) {
        Objects.requireNonNull(pattern);
        var value = read();
        if (value == null) {
            throw new NoSuchElementException(Message.NO_SUCH_TOKEN.format());
        }
        var matcher = pattern.matcher(value);
        if (matcher.matches()) {
            nextToken = null;
            return new DefaultToken(value);
        }
        if (matcher.lookingAt()) {
            value = matcher.group();
            nextToken = nextToken.substring(matcher.end());
            return new DefaultToken(value);
        }
        throw new NoSuchElementException(Message.NO_SUCH_TOKEN_MATCHING_PATTERN.format(pattern));
    }

}
