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
package com.unitedjiga.common.parsing.util;

import com.unitedjiga.common.parsing.NonTerminalSymbol;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.SymbolVisitor;
import com.unitedjiga.common.parsing.TerminalSymbol;

/**
 * @author Mikami Junji
 *
 */
@FunctionalInterface
public interface SimpleSymbolVisitor<R, P> extends SymbolVisitor<R, P> {

    @Override
    public default R visitTerminal(TerminalSymbol s, P p) {
        return defaultAction(s, p);
    }

    @Override
    public default R visitNonTerminal(NonTerminalSymbol s, P p) {
        return defaultAction(s, p);
    }

    public R defaultAction(Symbol s, P p);
}
