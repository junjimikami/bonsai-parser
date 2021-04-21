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

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.NonTerminalSymbol;
import com.unitedjiga.common.parsing.ParsingException;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.SequentialProduction;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.SymbolVisitor;
import com.unitedjiga.common.parsing.TerminalSymbol;
import com.unitedjiga.common.parsing.Tokenizer;

public class SequenctialProductionTest {

    SymbolVisitor<Void, String> visitor = new SymbolVisitor<>() {

        @Override
        public Void visitTerminal(TerminalSymbol s, String p) {
            System.out.printf("%s%s=%s\n", p, s.getKind(), s);
            return null;
        }

        @Override
        public Void visitNonTerminal(NonTerminalSymbol s, String p) {
            System.out.printf("%s%s=%s\n", p, s.getKind(), s);
            s.forEach(e->e.accept(this, "  "+p));
            return null;
        }
    };

    private void testThrowing(Production prd, String...strings) {
        var pser = prd.parser(Tokenizer.wrap(strings));
        assertThrows(ParsingException.class, ()->pser.parse())
                .printStackTrace(System.out);
    }

    @Test
    void testEmpty() throws Exception {
        var prd = SequentialProduction.builder()
                .build();
        {
            var pser = prd.parser(Tokenizer.wrap());
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        testThrowing(prd, "A");
        testThrowing(prd, "");
    }
    
    @Test
    void test01() throws Exception {
        var prd = SequentialProduction.builder()
                .add("[0-9]")
                .build();
        testThrowing(prd);
        testThrowing(prd, "A");
        testThrowing(prd, "");
        {
            var pser = prd.parser(Tokenizer.wrap("0"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("0", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
            assertThrows(ParsingException.class, ()->pser.parse())
                    .printStackTrace(System.out);
        }
        testThrowing(prd, "0", "1");
    }

    @Test
    void test02() throws Exception {
        var prd = SequentialProduction.builder()
                .add("[0-9]")
                .add("[A-Z]")
                .build();
        testThrowing(prd);
        testThrowing(prd, "0");
        testThrowing(prd, "A");
        testThrowing(prd, "");
        {
            var pser = prd.parser(Tokenizer.wrap("0", "A"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("0A", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
            assertThrows(ParsingException.class, ()->pser.parse())
                    .printStackTrace(System.out);
        }
        testThrowing(prd, "0", "A", "");
    }

    @Test
    void test03() throws Exception {
        var prd0 = SequentialProduction.builder()
                .add("[0-9]")
                .add("[A-Z]")
                .build();
        var prd = SequentialProduction.builder()
                .add(prd0)
                .build();
        testThrowing(prd);
        testThrowing(prd, "0");
        testThrowing(prd, "A");
        testThrowing(prd, "");
        {
            var pser = prd.parser(Tokenizer.wrap("0", "A"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("0A", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
            assertThrows(ParsingException.class, ()->pser.parse())
                    .printStackTrace(System.out);
        }
        testThrowing(prd, "0", "A", "");
    }

    @Test
    void test04() throws Exception {
        Supplier<Production> prd0 = ()->SequentialProduction.builder()
                .add("[0-9]")
                .add("[A-Z]")
                .build();
        var prd = SequentialProduction.builder()
                .add(prd0)
                .build();
        testThrowing(prd);
        testThrowing(prd, "0");
        testThrowing(prd, "A");
        testThrowing(prd, "");
        {
            var pser = prd.parser(Tokenizer.wrap("0", "A"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("0A", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
            assertThrows(ParsingException.class, ()->pser.parse())
                    .printStackTrace(System.out);
        }
        testThrowing(prd, "0", "A", "");
    }
}
