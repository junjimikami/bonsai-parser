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

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import com.unitedjiga.common.parsing.impl.AbstractTokenizer;

/**
 *
 * @author Junji Mikami
 */
public interface Tokenizer extends Iterator<Token>, Closeable {

    /**
     * 
     * @param it
     * @return
     */
    static Tokenizer wrap(Iterator<? extends CharSequence> it) {
        return new AbstractTokenizer(it) {
            
            @Override
            public void close() {
                try {
                    if (it instanceof Closeable) {
                        ((Closeable) it).close();
                    }
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            }
        };
    }

    /**
     * 
     * @param str
     * @return
     */
    static Tokenizer wrap(String... str) {
        return wrap(Arrays.asList(str).iterator());
    }

    @Override
    boolean hasNext();

    @Override
    Token next();

    @Override
    void close();

    @Override
    String toString();

    /**
     * 
     * @return
     */
    Buffer buffer();

    /**
     * 
     * @author Junji Mikami
     *
     */
    interface Buffer {
        
        /**
         * 
         * @return
         */
        boolean hasRemaining();
        
        /**
         * 
         * @return
         * @throws java.util.NoSuchElementException 元となるトークナイザーにこれ以上トークンがない場合
         */
        Token get();
        
        void pushBack();
        void reset();
        
        /**
         * 
         * @return
         */
        boolean isEmpty();
        
        /**
         * 
         * @return
         * @throws java.util.NoSuchElementException バッファが空の場合
         */
        Token remove();
        
        Stream<Token> tokens();
    }
}
