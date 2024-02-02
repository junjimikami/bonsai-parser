package com.unitedjiga.common.parsing.grammar;

import java.util.NoSuchElementException;

final class Stubs {

    static final Expression DUMMY_EXPRESSION = expression("DUMMY_EXPRESSION");

    static final Expression.Builder DUMMY_EXPRESSION_BUILDER = builderOf(DUMMY_EXPRESSION);

    static final ProductionSet DUMMY_PRODUCTION_SET = new ProductionSet() {

        @Override
        public boolean containsSymbol(String symbol) {
            return true;
        }

        @Override
        public Production get(String symbol) {
            return productionOf(symbol);
        }
    };
    static final ProductionSet EMPTY_PRODUCTION_SET = new ProductionSet() {

        @Override
        public boolean containsSymbol(String symbol) {
            return false;
        }

        @Override
        public Production get(String symbol) {
            throw new NoSuchElementException("TEST CODE");
        }
    };

    static Expression expression(String s) {
        return new Expression() {

            @Override
            public Kind getKind() {
                throw new AssertionError();
            }

            @Override
            public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p) {
                throw new AssertionError();
            }

            @Override
            public String toString() {
                return s;
            }
        };
    }

    static Expression.Builder builderOf(Expression expression) {
        return new Expression.Builder() {

            @Override
            public Expression build(ProductionSet set) {
                return expression;
            }
        };
    }

    static Production productionOf(String symbol) {
        return new Production() {

            @Override
            public String getSymbol() {
                return symbol;
            }

            @Override
            public Expression getExpression() {
                return expression(symbol);
            }
        };
    }
}
