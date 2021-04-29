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

import java.util.Collections;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.AlternativeProduction;
import com.unitedjiga.common.parsing.NonTerminalSymbol;
import com.unitedjiga.common.parsing.ParsingException;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.SequentialProduction;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.SymbolVisitor;
import com.unitedjiga.common.parsing.TerminalSymbol;
import com.unitedjiga.common.parsing.Tokenizer;
import static com.unitedjiga.common.parsing.AlternativeProduction.builder;

public class AlternativeProductionTest {

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
        var prd = AlternativeProduction.builder()
                .build();
        var pser = prd.parser(Tokenizer.wrap());
        var smbl = pser.parse();
        assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
        assertEquals("", smbl.asToken().getValue());
        smbl.accept(visitor, "+-");

        testThrowing(prd, "A");
        testThrowing(prd, "");
    }
    @Test
    void test01() throws Exception {
        var prd = AlternativeProduction.builder()
                .add("")
                .build();
        var pser = prd.parser(Tokenizer.wrap(""));
        var smbl = pser.parse();
        assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
        assertEquals("", smbl.asToken().getValue());
        smbl.accept(visitor, "+-");

        testThrowing(prd);
        testThrowing(prd, "A");
    }
    @Test
    void test02() throws Exception {
        var prd = AlternativeProduction.builder()
                .add("0")
                .add("1")
                .build();
        {
            var pser = prd.parser(Tokenizer.wrap("0"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("0", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("1"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("1", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }

        testThrowing(prd);
        testThrowing(prd, "2");
        testThrowing(prd, "0", "1");
    }
    @Test
    void test03() throws Exception {
        var prd0 = AlternativeProduction.builder()
                .add("1")
                .add("2")
                .build();
        var prd = AlternativeProduction.builder()
                .add("0")
                .add(prd0)
                .build();
        {
            var pser = prd.parser(Tokenizer.wrap("0"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("0", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("1"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("1", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("2"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("2", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }

        testThrowing(prd);
        testThrowing(prd, "0", "1", "2");
    }
    @Test
    void test04() throws Exception {
        Supplier<Production> prd0 = () -> AlternativeProduction.builder()
                .add("1")
                .add("2")
                .build();
        var prd = AlternativeProduction.builder()
                .add("0")
                .add(prd0)
                .build();
        {
            var pser = prd.parser(Tokenizer.wrap("0"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("0", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("1"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("1", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("2"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("2", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }

        testThrowing(prd);
        testThrowing(prd, "0", "1", "2");
    }

    @Test
    void test05() throws Exception {
        var prd0 = AlternativeProduction.builder()
                .add("1")
                .add("2")
                .build();
        var prd = SequentialProduction.builder()
                .add("0")
                .add(prd0)
                .build();
        {
            var pser = prd.parser(Tokenizer.wrap("0", "1"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("01", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("0", "2"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("02", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }

        testThrowing(prd);
        testThrowing(prd, "0");
        testThrowing(prd, "0", "3");
        testThrowing(prd, "0", "1", "2");
    }
    @Test
    void testOption01() throws Exception {
        var prd0 = AlternativeProduction.builder()
                .build();
        var prd = AlternativeProduction.builder()
                .add("0")
                .add(prd0)
                .build();
        {
            var pser = prd.parser(Tokenizer.wrap("0"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("0", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap());
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }

        testThrowing(prd, "0", "");
    }
    @Test
    void testOption02() throws Exception {
        var prd0 = AlternativeProduction.builder()
                .build();
        var prd1 = AlternativeProduction.builder()
                .add("1")
                .add(prd0)
                .build();
        var prd = SequentialProduction.builder()
                .add("0")
                .add(prd1)
                .add("2")
                .build();
        {
            var pser = prd.parser(Tokenizer.wrap("0", "1", "2"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("012", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("0", "2"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("02", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }

    }
    
    Production empty() {
//        return AlternativeProduction.builder().build();
        return SequentialProduction.builder().build();
    }
    /** A=empty|B */
    Production prdA() {
        return AlternativeProduction.builder()
                .add(empty())
                .add(prdB())
                .build();
    }
    /** B=0A */
    Production prdB() {
        return SequentialProduction.builder()
                .add("0")
                .add(this::prdA)
                .build();
    }
    @Test
    void testRepetition() throws Exception {
        {
            var pser = prdA().parser(Tokenizer.wrap());
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prdA().parser(Tokenizer.wrap("0"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("0", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prdA().parser(Tokenizer.wrap("0", "0"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("00", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prdA().parser(Tokenizer.wrap(Collections.nCopies(8, "0").iterator()));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("00000000", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }

    }

    @Test
    void testOpt01() throws Exception {
        var prd = builder()
                .add("0")
                .add("1")
                .build()
                .opt();
        {
            var pser = prd.parser(Tokenizer.wrap("0"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("0", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("1"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("1", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap());
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        testThrowing(prd, "0", "1");
    }
    @Test
    void testOpt02() throws Exception {
        var prd = builder()
                .add("0")
                .add(builder()
                        .add("1")
                        .build()
                        .opt())
                .build();
        {
            var pser = prd.parser(Tokenizer.wrap("0"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("0", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("1"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("1", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap());
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        testThrowing(prd, "0", "1");
    }
    @Test
    void testRepeat01() throws Exception {
        var prd = builder()
                .add("0")
                .add("1")
                .build()
                .repeat();
        {
            var pser = prd.parser(Tokenizer.wrap("0"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("0", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("1"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("1", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap());
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("01101010".split("\\B")));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.NON_TERMINAL, smbl.getKind());
            assertEquals("01101010", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
    }
    @Test
    void testRepeat02() throws Exception {
        var prd = builder()
                .add("0")
                .add(builder()
                        .add("1")
                        .build()
                        .repeat())
                .build();
        {
            var pser = prd.parser(Tokenizer.wrap("0"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("0", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("1"));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("1", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap());
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        {
            var pser = prd.parser(Tokenizer.wrap("11111".split("\\B")));
            var smbl = pser.parse();
            assertEquals(Symbol.Kind.SINGLETON, smbl.getKind());
            assertEquals("11111", smbl.asToken().getValue());
            smbl.accept(visitor, "+-");
        }
        testThrowing(prd, "0101".split("\\B"));
    }
}
