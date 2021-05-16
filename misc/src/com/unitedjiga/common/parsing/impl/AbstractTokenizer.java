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

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
abstract class AbstractTokenizer implements Tokenizer {

    @Override
    public Buffer buffer() {
        return new SimpleBuffer(this);
    }

    static class SimpleBuffer implements Tokenizer.Buffer {
        private Tokenizer tzer;
        private List<Token> buffer = new LinkedList<>();
        private int current = 0;

        private SimpleBuffer(Tokenizer tzer) {
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
                throw new NoSuchElementException(Message.CANNOT_PUSHBACK.format());
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
            if (current <= 0) {
                throw new NoSuchElementException(Message.CANNOT_REMOVE.format());
            }
            return buffer.subList(0, current--).remove(0);
        }

//        @Override
//        public Stream<Token> tokens() {
//            return buffer.subList(0, current).stream();
//        }
        
        @Override
        public String toString() {
            // Buffered tokens:[a, b, c] << Remaining:[d, e, f, ...]
            StringBuilder sb = new StringBuilder();
            sb.append(buffer.subList(0, current).stream()
                    .map(Token::getValue)
                    .collect(Collectors.joining(", ", "Buffered tokens:[", "]")));
            sb.append(" << ");

            StringJoiner remaining = new StringJoiner(", ", "Remaining:[", "]");
            buffer.subList(current, buffer.size()).stream()
                    .map(Token::getValue)
                    .forEach(remaining::add);
            remaining.add(tzer.hasNext() ? "..." : "no tokens");
            sb.append(remaining.toString());
            return sb.toString();
        }
    }
}
