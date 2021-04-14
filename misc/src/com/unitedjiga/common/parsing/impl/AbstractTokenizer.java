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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
public abstract class AbstractTokenizer implements Tokenizer {

    private Iterator<? extends CharSequence> it;

    public AbstractTokenizer(Iterator<? extends CharSequence> it) {
        this.it = Objects.requireNonNull(it);
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public Token next() {
        return Token.of(it.next().toString());
    }

    @Override
    public Buffer buffer() {
        return new SimpleBuffer(this);
    }

    public static class SimpleBuffer implements Tokenizer.Buffer {
        private Tokenizer tzer;
        private List<Token> buffer = new LinkedList<>();
        private int current = 0;

        public SimpleBuffer(Tokenizer tzer) {
            this.tzer = Objects.requireNonNull(tzer);
        }

        @Override
        public boolean hasRemaining() {
            return current < buffer.size() || tzer.hasNext();
        }

        @Override
        public Token get() {
            if (buffer.size() <= current) {
                buffer.add(tzer.next());
            }
            return buffer.get(current++);
        }

        @Override
        public void pushBack() {
            if (current <= 0) {
                throw new NoSuchElementException();
            }
            current--;
        }

        @Override
        public void reset() {
            current = 0;
        }

        @Override
        public boolean isEmpty() {
            return buffer.subList(0, current).isEmpty();
        }

        @Override
        public Token remove() {
            try {
                return buffer.subList(0, current--).remove(0);
            } catch (IndexOutOfBoundsException ex) {
                throw new NoSuchElementException(ex);
            }
        }

        @Override
        public Stream<Token> tokens() {
            return buffer.subList(0, current).stream();
        }
        
        @Override
        public String toString() {
            return IntStream.range(0, buffer.size())
                    .mapToObj(i -> (i == current ? "-> " : "") + buffer.get(i).getValue())
                    .collect(Collectors.joining(", ", "[", "]"))
                    .concat(current == buffer.size() ? " ->" : "")
                    .concat(tzer.hasNext() ? " Tokenizer" : " EOF");
        }
    }
}
