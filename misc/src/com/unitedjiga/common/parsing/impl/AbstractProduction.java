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

import java.util.Objects;

import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.QuantifiedProduction;

/**
 *
 * @author Junji Mikami
 */
abstract class AbstractProduction implements Production, Cloneable {

    static abstract class Builder implements Production.Builder {
        protected final String name;
        protected boolean isBuilt;
    
        Builder() {
            this.name = null;
        }
        Builder(String name) {
            this.name = Objects.requireNonNull(name);
        }
        @Override
        public QuantifiedProduction.Builder opt() {
            return new OptProduction.Builder(this);
        }
        @Override
        public QuantifiedProduction.Builder repeat() {
            return new RepeatProduction.Builder(this);
        }
        protected void check() {
            if (isBuilt) {
                throw new IllegalStateException(Message.ALREADY_BUILT.format());
            }
        }
        protected void checkForBuild() {
            check();
            isBuilt = true;
        }
    }

//    private static final TermProduction EOF = new TermProduction("") {
//        @Override
//        public boolean matches(Token t) {
//            return false;
//        }
//        @Override
//        public String toString() {
//            return "(EOF)";
//        }
//    };

//    @Override
//    public Parser parser(Tokenizer tokenizer) {
//        Objects.requireNonNull(tokenizer, Message.REQUIRE_NON_NULL.format());
//        return new Parser() {
//            
//            @Override
//            public Symbol parse() {
//                Symbol s = interpret(tokenizer);
//                if (tokenizer.hasRemaining()) {
//                    throw newException(Message.TOO_MANY_TOKEN, tokenizer);
//                }
//                return s;
//            }
//
//            @Override
//            public Stream<Symbol> iterativeParse() {
//                return Stream.generate(() -> tokenizer.hasRemaining() ? interpret(tokenizer) : null)
//                        .takeWhile(Objects::nonNull);
//            }
//
//            private Symbol interpret(Tokenizer tokenizer) {
//                return AbstractProduction.this.interpret(tokenizer, Collections.singleton(EOF));
//            }
//        };
//    }

    private String name;

    AbstractProduction() {
        this.name = null;
    }
    AbstractProduction(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    @Override
    public Production as(String name) {
        Objects.requireNonNull(name);
        var c = clone();
        c.name = name;
        return c;
    }

    @Override
    protected AbstractProduction clone() {
        try {
            return (AbstractProduction) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

	@Override
    public QuantifiedProduction opt() {
        return new OptProduction(this);
    }

    @Override
    public QuantifiedProduction repeat() {
        return new RepeatProduction(this);
    }
}
