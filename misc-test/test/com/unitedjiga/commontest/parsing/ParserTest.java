/*
 * The MIT License
 *
 * Copyright 2021 Junji Mikami.
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
package com.unitedjiga.commontest.parsing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.NonTerminalSymbol;
import com.unitedjiga.common.parsing.ParserFactory;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.SymbolVisitor;
import com.unitedjiga.common.parsing.TerminalSymbol;
import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
class ParserTest {

//    @Test
//    void test() {
//        var pf = ParserFactory.loadFactory("com.unitedjiga.commontest.parsing.sp.TestParserFactory");
//        var pser = pf.createParser(Tokenizer.wrap("12345".split("\\B")));
//        var smbl = pser.parse();
//
//        assertTrue(smbl.getKind().isNonTerminal());
//        var it = smbl.accept(new SymbolVisitor<Iterator<Token>, Void>() {
//            @Override
//            public Iterator<Token> visitTerminal(TerminalSymbol s, Void p) {
//                return null;
//            }
//            @Override
//            public Iterator<Token> visitNonTerminal(NonTerminalSymbol s, Void p) {
//                return s.stream().map(Symbol::asToken).iterator();
//            }
//        }, null);
//        assertTrue(it.hasNext());
//        assertEquals("1", it.next().getValue());
//        assertEquals("2", it.next().getValue());
//        assertEquals("3", it.next().getValue());
//        assertEquals("4", it.next().getValue());
//        assertEquals("5", it.next().getValue());
//        assertFalse(it.hasNext());
//    }

}
