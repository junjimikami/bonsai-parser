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
package com.unitedjiga.common.parsing;

import java.io.Reader;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 *
 * @author Junji Mikami
 */
public interface Tokenizer {
    public static interface Builder {
        public Tokenizer.Builder set(Reader r);
        public Tokenizer.Builder set(Iterator<String> it);
        public Tokenizer.Builder filter(Predicate<Token> p);
        public Tokenizer build();
    }

//    /**
//     * 
//     * @param it
//     * @return
//     */
//    public static Tokenizer wrap(Iterator<? extends CharSequence> it) {
//        return Tokenizers.createTokenizer(it);
//    }
//
//    /**
//     * 
//     * @param str
//     * @return
//     */
//    public static Tokenizer wrap(String... str) {
//        return wrap(Arrays.asList(str).iterator());
//    }

    public Token read();

    public boolean hasNext();
    public Token next();

    public boolean hasPrevious();
    public Token previous();

    public default Token peek() {
        next();
        return previous();
    }
}
