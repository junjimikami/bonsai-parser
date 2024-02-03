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

import java.io.Reader;

import com.unitedjiga.common.parsing.Parser;
import com.unitedjiga.common.parsing.ParserFactory;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.grammar.Grammar;

/**
 * @author Mikami Junji
 *
 */
class DefaultParserFactory implements ParserFactory {
    private final Grammar grammar;

    DefaultParserFactory(Grammar grammar) {
        assert grammar != null;
        this.grammar = grammar;
    }

    @Override
    public Parser createParser(Tokenizer tokenizer) {
        assert tokenizer != null;
        return new DefaultParser(grammar, tokenizer);
    }

    @Override
    public Parser createParser(Reader reader) {
        var tokenizer = new ReaderTokenizer(reader);
        return createParser(tokenizer);
    }

}
