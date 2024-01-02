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
package com.unitedjiga.commontest.parsing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.ChoiceExpression;
import com.unitedjiga.common.parsing.PatternProduction;
import com.unitedjiga.common.parsing.ProductionFactory;
import com.unitedjiga.common.parsing.QuantifiedProduction;
import com.unitedjiga.common.parsing.Reference;
import com.unitedjiga.common.parsing.SequenceExpression;

/**
 * @author Mikami Junji
 *
 */
class ProductionFactoryTest {

    @Test
    void test() {
        var pf = ProductionFactory.newFactory();

        var ptn1 = pf.createPattern("[0-9]");
        assertInstanceOf(PatternProduction.class, ptn1);
        assertEquals("[0-9]", ptn1.getPattern().pattern());
        assertEquals(0, ptn1.getPattern().flags());

        var ptn2 = pf.createPattern("[0-9]", Pattern.LITERAL);
        assertInstanceOf(PatternProduction.class, ptn2);
        assertEquals("[0-9]", ptn2.getPattern().pattern());
        assertEquals(Pattern.LITERAL, ptn2.getPattern().flags() & Pattern.LITERAL);

        var ptn3 = pf.createPatternBuilder()
                .setPattern("[0-9]")
                .setFlags(Pattern.LITERAL)
                .build();
        assertInstanceOf(PatternProduction.class, ptn3);
        assertEquals("[0-9]", ptn3.getPattern().pattern());
        assertEquals(Pattern.LITERAL, ptn3.getPattern().flags() & Pattern.LITERAL);

        var alt1 = pf.createAlternativeBuilder().build();
        assertInstanceOf(ChoiceExpression.class, alt1);
        assertTrue(alt1.getChoices().isEmpty());

        var seq1 = pf.createSequentialBuilder().build();
        assertInstanceOf(SequenceExpression.class, seq1);
        assertTrue(seq1.getSequence().isEmpty());

        var ref1 = pf.createReference(() -> ptn1);
        assertInstanceOf(Reference.class, ref1);
        assertEquals(ptn1, ref1.get());

        var opt1 = pf.createOptional(ptn1);
        assertInstanceOf(QuantifiedProduction.class, opt1);
        assertEquals(ptn1, opt1.stream().findFirst().get());
        assertEquals(1, opt1.stream().count());
        assertEquals(0, opt1.getLowerLimit());
        assertEquals(1, opt1.getUpperLimit().getAsLong());

        var rep1 = pf.createZeroOrMore(ptn1);
        assertInstanceOf(QuantifiedProduction.class, rep1);
        assertEquals(ptn1, rep1.stream().findFirst().get());
        assertEquals(0, rep1.getLowerLimit());
        assertTrue(rep1.getUpperLimit().isEmpty());
    }

//    @Test
//    void test2() throws Exception {
//        var pf = ProductionFactory.newFactory();
//        
//        pf.register()
//        .add("ptn1", pf.createPattern("[0-9]"))
//        .add("ptn2", pf.createPattern("[0-9]", Pattern.LITERAL))
//        .add("alt1", pf.createAlternativeBuilder())
//        .add("seq1", pf.createSequentialBuilder())
//        .add("ref1", pf.createReference("ptn1"));
//        
//        assertInstanceOf(PatternProduction.class, pf.register().get("ptn1"));
//        assertInstanceOf(PatternProduction.class, pf.register().get("ptn2"));
//        assertInstanceOf(AlternativeProduction.class, pf.register().get("alt1"));
//        assertInstanceOf(SequentialProduction.class, pf.register().get("seq1"));
//        assertInstanceOf(Reference.class, pf.register().get("ref1"));
//        var ref = (Reference<?>) pf.register().get("ref1");
//        assertEquals(pf.register().get("ptn1"), ref.get());
//    }
}
