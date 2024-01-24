package com.unitedjiga.common.parsing.grammar.impl;

import java.util.Objects;

import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.Production;

class DefaultProduction implements Production {

    private final String symbol;
    private final Expression expression;

    /**
     * @param symbol
     * @param expression
     */
    DefaultProduction(String symbol, Expression expression) {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(expression);
        this.symbol = symbol;
        this.expression = expression;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "%s::=%s".formatted(symbol, expression);
    }
}
