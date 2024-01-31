package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.fail;

interface TestExpressionVisitor<R, P> extends SimpleExpressionVisitor<R, P> {

    @Override
    default R defaultAction(Expression expression, P p) {
        return fail();
    }
}
