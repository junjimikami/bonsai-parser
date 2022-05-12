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

import java.util.Objects;

import com.unitedjiga.common.parsing.Parser;
import com.unitedjiga.common.parsing.ParserFactory;
import com.unitedjiga.common.parsing.ParsingException;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.StreamParser;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 * @author Mikami Junji
 *
 */
class DefaultParserFactory implements ParserFactory {
    private final Production production;

    DefaultParserFactory(Production p) {
        Objects.requireNonNull(p);
        this.production = p;
    }

    @Override
    public Parser createParser(Tokenizer tokenizer) {
        Objects.requireNonNull(tokenizer);
        return new Parser() {
            private Interpreter interpreter = new Interpreter();
            @Override
            public Symbol parse() {
                var s = interpreter.interpret(production, tokenizer);
                if (tokenizer.hasNext()) {
                    throw new ParsingException();
                }
                return s;
            }
        };
    }

    @Override
    public StreamParser createStreamParser(Tokenizer tokenizer) {
        // TODO Auto-generated method stub
        return null;
    }

}
