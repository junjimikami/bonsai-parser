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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.ParserFactory;
import com.unitedjiga.common.parsing.Expression;
import com.unitedjiga.common.parsing.ProductionFactory;
import com.unitedjiga.common.parsing.ReferenceExpression;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.util.SimpleProductionVisitor;

/**
 * @author Junji Mikami
 *
 */
class ParserTest {
    @Test
    void testName() throws Exception {
        var prdf = ProductionFactory.newFactory();
        var grammar = new HashMap<String, Expression>();
        grammar.put("Type", prdf.createAlternativeBuilder()
                .add(grammar.get("PrimitiveType"))
                .add(grammar.get("ReferenceType"))
                .build());
        grammar.put("PrimitiveType", prdf.createAlternativeBuilder()
                .add(grammar.get("NumericType"))
                .add(prdf.createPattern("boolean"))
                .build());
        grammar.put("NumericType", prdf.createAlternativeBuilder()
                .add(grammar.get("IntegralType"))
                .add(grammar.get("FloatingPointType"))
                .build());
        grammar.put("IntegralType", prdf.createAlternativeBuilder()
                .add(prdf.createPattern("byte"))
                .add(prdf.createPattern("short"))
                .add(prdf.createPattern("long"))
                .add(prdf.createPattern("char"))
                .build());
        grammar.put("FloatingPointType", prdf.createAlternativeBuilder()
                .add(prdf.createPattern("float"))
                .add(prdf.createPattern("double"))
                .build());
        grammar.put("ReferenceType", prdf.createAlternativeBuilder()
                .add(grammar.get("ClassOrInterfaceType"))
                .add(grammar.get("TypeVariable"))
                .add(grammar.get("ArrayType"))
                .build());
        var psf = ParserFactory.newFactory(grammar.get("Type"));
        var tzer = (Tokenizer) null;
        var ps = psf.createParser(tzer);
        var s = ps.parseNonTerminal();
        Stream.of(s)
        .filter(e -> e.getName().equals("Type"))
        .flatMap(e -> e.getSymbols().stream())
        .filter(e -> e.getName().equals("PrimitiveType"))
//        .filter(e -> e.getName().equals("Type"))
//        .flatMap(e -> e.getSymbols().stream())
//        .filter(e -> e.getName().equals("PrimitiveType"))
//        .findFirst()
//        .map(e -> e.accept(Symbol.joining(), null))
//        .get()
        ;

        assertEquals("Type", s.getName());
    }
    
    @Test
    void testName2() throws Exception {
        class GrammarTest {
            ProductionFactory prdf = ProductionFactory.newFactory();

            Expression type() {
                return prdf.createAlternativeBuilder()
                        .setName("Type")
                        .add(prdf.createReference(this::primitiveType))
                        .add(prdf.createReference(this::referenceType))
                        .build();
            }
            Expression primitiveType() {
                return prdf.createAlternativeBuilder()
                        .setName("PrimitiveType")
                        .add(prdf.createReference(this::numericType))
                        .add(prdf.createPattern("boolean"))
                        .build();
            }
            Expression numericType() {
                return prdf.createAlternativeBuilder()
                        .setName("NumericType")
                        .add(prdf.createReference(this::integralType))
                        .add(prdf.createReference(this::floatingPointType))
                        .build();
            }
            Expression integralType() {
                return prdf.createAlternativeBuilder()
                        .setName("IntegralType")
                        .add(prdf.createPattern("byte"))
                        .add(prdf.createPattern("short"))
                        .add(prdf.createPattern("long"))
                        .add(prdf.createPattern("char"))
                        .build();
            }
            Expression floatingPointType() {
                return prdf.createAlternativeBuilder()
                        .setName("FloatingPointType")
                        .add(prdf.createPattern("float"))
                        .add(prdf.createPattern("double"))
                        .build(); 
            }
            Expression referenceType() {
                return prdf.createAlternativeBuilder()
                        .setName("ReferenceType")
                        .add(prdf.createReference(this::classOrInterfaceType))
                        .build();
            }
            Expression classOrInterfaceType() {
                return prdf.createAlternativeBuilder()
                        .setName("ClassOrInterfaceType")
                        .add(prdf.createSequentialBuilder()
                                .add(prdf.createQuantifiedBuilder()
                                        .set(prdf.createReference(null))
                                        .range(0, 1))
                                .add(prdf.createReference(null))
                                .add(prdf.createQuantifiedBuilder()
                                        .set(prdf.createReference(null))
                                        .range(0, 1)))
                        .add(prdf.createSequentialBuilder())
                        .build();
            }
        }
        var grammarTest = new GrammarTest();
        var psf = ParserFactory.newFactory(grammarTest.type());
//        psf.getGrammar();
        var tzer = (Tokenizer) null;
        var ps = psf.createParser(tzer);
        var s = ps.parseNonTerminal();
        Stream.of(s)
        .filter(e -> e.getName().equals("Type"))
        .flatMap(e -> e.getSymbols().stream())
        .filter(e -> e.getName().equals("PrimitiveType"))
//        .filter(e -> e.getName().equals("Type"))
//        .flatMap(e -> e.getSymbols().stream())
//        .filter(e -> e.getName().equals("PrimitiveType"))
//        .findFirst()
//        .map(e -> e.accept(Symbol.joining(), null))
//        .get()
        ;
    }
    

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
