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

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;

import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.TokenizerFactory;

/**
 * 
 * @author Junji Mikami
 *
 */
public class DefaultTokenizerFactory implements TokenizerFactory {

    private static class CharToken implements Token {
        private int ch;

        public CharToken(int ch) {
            this.ch = ch;
        }

        @Override
        public String getValue() {
            return Character.toString(ch);
        }

        @Override
        public String toString() {
            return getValue();
        }
    }

    @Override
    public Tokenizer createTokenizer(Reader r) {
        return new Tokenizer() {
            private final PushbackReader pr = new PushbackReader(r);

            @Override
            public void close() {
                try {
                    pr.close();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public Token peek() {
                int ch = peekChar();
                if (ch != -1) {
                    return new CharToken(ch);
                }
                throw new NoSuchElementException();
            }

            @Override
            public Token next() {
                int ch = read();
                if (ch != -1) {
                    return new CharToken(ch);
                }
                throw new NoSuchElementException();
            }

            @Override
            public boolean hasNext() {
                return peekChar() != -1;
            }

            private int read() {
                try {
                    return pr.read();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            private void unread(int ch) {
                try {
                    pr.unread(ch);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private int peekChar() {
                int ch = -1;
                try {
                    ch = read();
                    return ch;
                } finally {
                    if (ch != -1) {
                        unread(ch);
                    }
                }
            }

            @Override
            public String toString() {
                return "Next: " + peekChar();
            }
        };
    }

}
