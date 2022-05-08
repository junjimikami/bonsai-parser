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

import com.unitedjiga.common.parsing.AlternativeProduction;
import com.unitedjiga.common.parsing.PatternProduction;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.ProductionFactory;
import com.unitedjiga.common.parsing.ProductionVisitor;
import com.unitedjiga.common.parsing.QuantifiedProduction;
import com.unitedjiga.common.parsing.SequentialProduction;
import com.unitedjiga.common.parsing.TerminalProduction;
import com.unitedjiga.common.parsing.Token;

/**
 * 
 * @author Junji Mikami
 *
 */
public final class Productions {

    private static final TerminalProduction EMPTY =  new TerminalProduction() {

        @Override
        public <R, P> R accept(ProductionVisitor<R, P> visitor, P p) {
            return visitor.visitEmpty(this, p);
        }

        @Override
        public String getName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Kind getKind() {
            return Kind.EMPTY;
        }

        @Override
        public Production as(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public QuantifiedProduction opt() {
            throw new UnsupportedOperationException();
        }

        @Override
        public QuantifiedProduction repeat() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean matches(Token t) {
            return false;
        }
        
    };

    private Productions() {
    }

    public static ProductionFactory newFactory() {
    	return new DefaultProductionFactory();
    }

    public static PatternProduction ofPattern(String regex) {
    	return new TermProduction(regex);
    }
    public static PatternProduction ofPattern(String regex, int flags) {
    	return new TermProduction(regex, flags);
    }

    public static SequentialProduction.Builder sequentialBuilder() {
        return new SeqProduction.Builder();
    }
    public static SequentialProduction.Builder sequentialBuilder(String name) {
        return new SeqProduction.Builder(name);
    }
    
    public static AlternativeProduction.Builder alternativeBuilder() {
        return new AltProduction.Builder();
    }
    public static AlternativeProduction.Builder alternativeBuilder(String name) {
        return new AltProduction.Builder(name);
    }

    public static TerminalProduction empty() {
        return EMPTY;
    }

    public static SequentialProduction of(Object... args) {
        SequentialProduction.Builder builder = sequentialBuilder();
        for (Object o : args) {
            if (o instanceof Production) {
                builder.add((Production) o);
//            } else if (o instanceof String) {
//                builder.add((String) o);
//            } else if (o instanceof Supplier) {
//                builder.add((Supplier<? extends Production>) o);
//            } else {
//                builder.add(o.toString());
            }
        }
        return builder.build();
    }

    public static AlternativeProduction oneOf(Object... args) {
        AlternativeProduction.Builder builder = alternativeBuilder();
        for (Object o : args) {
            if (o instanceof Production) {
                builder.add((Production) o);
//            } else if (o instanceof String) {
//                builder.add((String) o);
//            } else if (o instanceof Supplier) {
//                builder.add((Supplier<? extends Production>) o);
//            } else {
//                builder.add(o.toString());
            }
        }
        return builder.build();
    }

}
