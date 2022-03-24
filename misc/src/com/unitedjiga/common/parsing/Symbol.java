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

/**
 *
 * @author Junji Mikami
 */
public interface Symbol {

    /**
     * 
     * @author Junji Mikami
     *
     */
    enum Kind {
        TERMINAL,
        NON_TERMINAL,
        SINGLETON;

        public boolean isTerminal() {
            return this == TERMINAL;
        }

        public boolean isNonTerminal() {
            return this == NON_TERMINAL || this == SINGLETON;
        }

        public boolean isSingleton() {
            return this == SINGLETON;
        }
    }

    /**
     * 
     * @return
     */
    Kind getKind();

    /**
     * 
     * @return
     */
    default Token asToken() {
        return Token.of(toString());
    }

    /**
     * 
     * @param <R>
     * @param <P>
     * @param v
     * @param p
     * @return
     */
    <R, P> R accept(SymbolVisitor<R, P> v, P p);

    @Override
    String toString();
}
