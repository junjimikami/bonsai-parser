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
import static com.unitedjiga.common.parsing.SequentialProduction.builder;

public class SequenctialProductionTest {

//    SymbolVisitor<Void, String> visitor = new SymbolVisitor<>() {
//
//        @Override
//        public Void visitTerminal(TerminalSymbol s, String p) {
//            System.out.printf("%s%s=%s\n", p, s.getKind(), s);
//            return null;
//        }
//
//        @Override
//        public Void visitNonTerminal(NonTerminalSymbol s, String p) {
//            System.out.printf("%s%s=%s\n", p, s.getKind(), s);
//            s.forEach(e->e.accept(this, "  "+p));
//            return null;
//        }
//    };
//
//    private void testThrowing(Production prd, String...strings) {
//        var pser = prd.parser(Tokenizer.wrap(strings));
//        assertThrows(ParsingException.class, ()->pser.parse())
//                .printStackTrace(System.out);
//    }
//
//    @Test
//    void testEmpty() throws Exception {
//        var prd = SequentialProduction.builder()
//                .build();
//        {
//            var pser = prd.parser(Tokenizer.wrap());
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        testThrowing(prd, "A");
//        testThrowing(prd, "");
//    }
//    
//    @Test
//    void test01() throws Exception {
//        var prd = SequentialProduction.builder()
//                .add("[0-9]")
//                .build();
//        {
//            var pser = prd.parser(Tokenizer.wrap("0"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("0", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//            assertThrows(ParsingException.class, ()->pser.parse())
//                    .printStackTrace(System.out);
//        }
//        testThrowing(prd);
//        testThrowing(prd, "A");
//        testThrowing(prd, "");
//        testThrowing(prd, "0", "1");
//    }
//
//    @Test
//    void test02() throws Exception {
//        var prd = SequentialProduction.builder()
//                .add("[0-9]")
//                .add("[A-Z]")
//                .build();
//        {
//            var pser = prd.parser(Tokenizer.wrap("0", "A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("0A", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//            assertThrows(ParsingException.class, ()->pser.parse())
//                    .printStackTrace(System.out);
//        }
//        testThrowing(prd);
//        testThrowing(prd, "0");
//        testThrowing(prd, "A");
//        testThrowing(prd, "");
//        testThrowing(prd, "0", "A", "");
//    }
//
//    @Test
//    void test03() throws Exception {
//        var prd0 = SequentialProduction.builder()
//                .add("[0-9]")
//                .add("[A-Z]")
//                .build();
//        var prd = SequentialProduction.builder()
//                .add(prd0)
//                .build();
//        {
//            var pser = prd.parser(Tokenizer.wrap("0", "A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("0A", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//            assertThrows(ParsingException.class, ()->pser.parse())
//                    .printStackTrace(System.out);
//        }
//        testThrowing(prd);
//        testThrowing(prd, "0");
//        testThrowing(prd, "A");
//        testThrowing(prd, "");
//        testThrowing(prd, "0", "A", "");
//    }
//
//    @Test
//    void test04() throws Exception {
//        Supplier<Production> prd0 = ()->SequentialProduction.builder()
//                .add("[0-9]")
//                .add("[A-Z]")
//                .build();
//        var prd = SequentialProduction.builder()
//                .add(prd0)
//                .build();
//        {
//            var pser = prd.parser(Tokenizer.wrap("0", "A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("0A", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//            assertThrows(ParsingException.class, ()->pser.parse())
//                    .printStackTrace(System.out);
//        }
//        testThrowing(prd);
//        testThrowing(prd, "0");
//        testThrowing(prd, "A");
//        testThrowing(prd, "");
//        testThrowing(prd, "0", "A", "");
//    }
//    @Test
//    void test05() throws Exception {
//        var prd0 = SequentialProduction.builder()
//                .build();
//        var prd = SequentialProduction.builder()
//                .add(prd0)
//                .add("A")
//                .build();
//        {
//            var pser = prd.parser(Tokenizer.wrap("A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("A", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//            assertThrows(ParsingException.class, ()->pser.parse())
//                    .printStackTrace(System.out);
//        }
//        testThrowing(prd);
//        testThrowing(prd, "");
//        testThrowing(prd, "A", "");
//    }
//    @Test
//    void testOpt01() throws Exception {
//        var prd = builder()
//                .add("A")
//                .build()
//                .opt();
//        {
//            var pser = prd.parser(Tokenizer.wrap("A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
//            assertEquals("A", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
////            assertThrows(ParsingException.class, ()->pser.parse())
////                    .printStackTrace(System.out);
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap());
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
//            assertEquals("", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
////            assertThrows(ParsingException.class, ()->pser.parse())
////                    .printStackTrace(System.out);
//        }
//        testThrowing(prd, "");
//    }
//    @Test
//    void testOpt02() throws Exception {
//        var prd = builder()
//                .add("A")
//                .add(builder()
//                        .add("B")
//                        .build()
//                        .opt())
//                .build();
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "B"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("AB", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("A", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//    }
//    @Test
//    void testOpt03() throws Exception {
//        var prd = builder()
//                .add(builder()
//                        .add("A")
//                        .build()
//                        .opt())
//                .add("B")
//                .build();
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "B"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("AB", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("B"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("B", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//    }
//    
//    Production prdA() {
//        return builder()
//                .add("A")
//                .add(this::prdA)
//                .build()
//                .opt();
//    }
//    @Test
//    void testOpt04() throws Exception {
//        var prd = prdA();
//        {
//            var pser = prd.parser(Tokenizer.wrap());
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
//            assertEquals("", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
//            assertEquals("A", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
//            assertEquals("AA", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "A", "B"));
//            assertThrows(ParsingException.class, pser::parse)
//                    .printStackTrace(System.out);
//        }
//    }
//
//    @Test
//    void testRepeat01() throws Exception {
//        var prd = builder()
//                .add("A")
//                .build()
//                .repeat();
//        {
//            var pser = prd.parser(Tokenizer.wrap("A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("A", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("AA", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "A", "B"));
//            assertThrows(ParsingException.class, pser::parse)
//                    .printStackTrace(System.out);
//        }
//    }
//    @Test
//    void testRepeat02() throws Exception {
//        var prd = builder()
//                .add(builder()
//                        .add("A")
//                        .add(builder()
//                                .add("B")
//                                .build()
//                                .opt())
//                        .build()
//                        .repeat())
//                .build();
//        {
//            var pser = prd.parser(Tokenizer.wrap());
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("A", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "B"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("AB", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "B", "A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("ABA", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "B", "A", "B"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("ABAB", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "A", "A", "B"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
//            assertEquals("AAAB", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//    }
//
//    Production prdB() {
//        // prdB = [A [B prdB]]
//        return builder()
//                .add("A")
//                .add(builder()
//                        .add("B")
//                        .add(this::prdB)
//                        .build()
//                        .opt())
//                .build()
//                .opt();
//    }
//    @Test
//    void testRepeat03() throws Exception {
//        var prd = prdB();
//        {
//            var pser = prd.parser(Tokenizer.wrap());
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
//            assertEquals("", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
//            assertEquals("A", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "B"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
//            assertEquals("AB", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "B", "A"));
//            var smbl = pser.parse();
//            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
//            assertEquals("ABA", smbl.asToken().getValue());
//            smbl.accept(visitor, "+-");
//        }
//        {
//            var pser = prd.parser(Tokenizer.wrap("A", "B", "A", "A"));
//            assertThrows(ParsingException.class, pser::parse)
//                    .printStackTrace(System.out);
//        }
//    }
}
