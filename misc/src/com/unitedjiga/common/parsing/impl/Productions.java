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

import com.unitedjiga.common.parsing.ChoiceExpression;
import com.unitedjiga.common.parsing.PatternProduction;
import com.unitedjiga.common.parsing.Expression;
import com.unitedjiga.common.parsing.ProductionFactory;
import com.unitedjiga.common.parsing.ProductionVisitor;
import com.unitedjiga.common.parsing.SequenceExpression;

/**
 * 
 * @author Junji Mikami
 *
 */
public final class Productions {

    private static final Expression EMPTY =  new Expression() {

        @Override
        public <R, P> R accept(ProductionVisitor<R, P> visitor, P p) {
            return visitor.visitEmpty(this, p);
        }

        @Override
        public Kind getKind() {
            return Kind.EMPTY;
        }
    };

    static final Expression EOF =  new Expression() {

        @Override
        public <R, P> R accept(ProductionVisitor<R, P> visitor, P p) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Kind getKind() {
            throw new UnsupportedOperationException();
        }
    };

    private Productions() {
    }

    public static ProductionFactory newFactory() {
    	return new DefaultProductionFactory();
    }

    public static PatternProduction ofPattern(String regex) {
//    	return new TermProduction(regex);
        throw new UnsupportedOperationException();
    }
    public static PatternProduction ofPattern(String regex, int flags) {
//    	return new TermProduction(regex, flags);
        throw new UnsupportedOperationException();
    }

    public static SequenceExpression.Builder sequentialBuilder() {
        return new SeqProduction.Builder();
    }
//    public static SequentialProduction.Builder sequentialBuilder(String name) {
//        return new SeqProduction.Builder().setName(name);
//    }
    
    public static ChoiceExpression.Builder alternativeBuilder() {
        return new AltProduction.Builder();
    }
//    public static AlternativeProduction.Builder alternativeBuilder(String name) {
//        return new AltProduction.Builder().setName(name);
//    }

    public static Expression empty() {
        return EMPTY;
    }

    public static SequenceExpression of(Object... args) {
        SequenceExpression.Builder builder = sequentialBuilder();
        for (Object o : args) {
            if (o instanceof Expression) {
                builder.add((Expression) o);
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

    public static ChoiceExpression oneOf(Object... args) {
        ChoiceExpression.Builder builder = alternativeBuilder();
        for (Object o : args) {
            if (o instanceof Expression) {
                builder.add((Expression) o);
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
