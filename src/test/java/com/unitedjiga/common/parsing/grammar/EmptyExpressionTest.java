package com.unitedjiga.common.parsing.grammar;

import com.unitedjiga.common.parsing.grammar.Expression.Kind;

class EmptyExpressionTest implements ExpressionTest {

    @Override
    public Expression build() {
        return Expression.EMPTY;
    }

    @Override
    public Kind kind() {
        return Kind.EMPTY;
    }

    @Override
    public ExpressionVisitor<Object[], String> elementVisitor() {
        return new TestExpressionVisitor<Object[], String>() {
            @Override
            public Object[] visitEmpty(Expression empty, String p) {
                return new Object[] { empty, p };
            }
        };
    }
}
