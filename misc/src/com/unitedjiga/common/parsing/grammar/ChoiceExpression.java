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
package com.unitedjiga.common.parsing.grammar;

import java.util.List;
import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.impl.GrammarService;

/**
 * @author Junji Mikami
 *
 */
public interface ChoiceExpression extends Expression {

    public static interface Builder extends Expression.Builder, Quantifiable {
        public ChoiceExpression.Builder add(Expression.Builder builder);
        public ChoiceExpression.Builder add(String reference);
        public ChoiceExpression.Builder addPattern(String regex);
        public ChoiceExpression.Builder addPattern(Pattern pattern);
        public ChoiceExpression.Builder addEmpty();
        public default ChoiceExpression build() {
            return build(null);
        }
        public ChoiceExpression build(ProductionSet set);
    }

    public static Builder builder() {
        return GrammarService.createChoiceBuilder();
    }

    @Override
    public default Kind getKind() {
    	return Kind.CHOICE;
    }

    @Override
    public default <R, P> R accept(ExpressionVisitor<R, P> visitor, P p) {
        return visitor.visitChoice(this, p);
    }

    public List<? extends Expression> getChoices();
}