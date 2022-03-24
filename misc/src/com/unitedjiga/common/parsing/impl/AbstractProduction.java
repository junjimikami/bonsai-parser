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

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.unitedjiga.common.parsing.AlternativeProduction;
import com.unitedjiga.common.parsing.Parser;
import com.unitedjiga.common.parsing.ParsingException;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.SequentialProduction;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 *
 * @author Junji Mikami
 */
abstract class AbstractProduction implements Production {

    private static final TermProduction EOF = new TermProduction("") {
        @Override
        boolean matches(Token t) {
            return false;
        }
        @Override
        public String toString() {
            return "(EOF)";
        }
    };

    @Override
    public Parser parser(Tokenizer tokenizer) {
        Objects.requireNonNull(tokenizer, Message.REQUIRE_NON_NULL.format());
        return new Parser() {
            
            @Override
            public Symbol parse() {
                Tokenizer.Buffer buf = tokenizer.buffer();
                Symbol s = interpret(buf);
                if (buf.hasRemaining()) {
                    throw newException(Message.TOO_MANY_TOKEN, buf);
                }
                return s;
            }

            @Override
            public Stream<Symbol> iterativeParse() {
                Tokenizer.Buffer buf = tokenizer.buffer();
                return Stream.generate(() -> buf.hasRemaining() ? interpret(buf) : null)
                        .takeWhile(Objects::nonNull);
            }

            private Symbol interpret(Tokenizer.Buffer buf) {
                return AbstractProduction.this.interpret(buf, Collections.singleton(EOF));
            }
        };
    }

    @Override
    public AlternativeProduction opt() {
        return new OptProduction(this);
    }

    @Override
    public SequentialProduction repeat() {
        return new RepeatProduction(this);
    }
    
    Set<TermProduction> getFirstSet() {
        return getFirstSet(Collections.emptySet());
    }

    abstract Set<TermProduction> getFirstSet(Set<TermProduction> followSet);

    abstract Symbol interpret(Tokenizer.Buffer buffer, Set<TermProduction> followSet);

    abstract boolean isOption();


    static boolean anyMatch(Set<TermProduction> set, Tokenizer.Buffer buffer) {
        if (!buffer.hasRemaining()) {
            return set.contains(EOF);
        }
        Token t = buffer.get();
        buffer.pushBack();
        return set.stream().anyMatch(p -> p.matches(t));
    }

    static ParsingException newException(Message m, Object... args) {
        Object[] str = Arrays.stream(args).map(e -> {
            if (e instanceof Tokenizer.Buffer) {
                Tokenizer.Buffer buf = (Tokenizer.Buffer) e;
                return buf.hasRemaining() ? "\"" + buf.get().getValue() + "\"" : "EOF";
            }
            return e.toString();
        }).toArray();
        return new ParsingException(m.format(str));
    }
}
