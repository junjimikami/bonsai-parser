package com.unitedjiga.common.parsing.grammar.impl;

import java.util.ArrayList;
import java.util.List;

import com.unitedjiga.common.parsing.grammar.Expression;

abstract class AbstractCompositeExpression extends AbstractExpression {
    static abstract class Builder extends QuantifiableBuilder {
        protected final List<Expression.Builder> builders = new ArrayList<>();

        @Override
        protected void checkForBuild() {
            super.checkForBuild();
            if (builders.isEmpty()) {
                throw new IllegalStateException(Message.NO_ELELEMNTS.format());
            }
        }

        @Override
        protected void checkForQuantifiable() {
            super.checkForQuantifiable();
            if (builders.isEmpty()) {
                throw new IllegalStateException(Message.NO_ELELEMNTS.format());
            }
        }
    }

    protected final List<? extends Expression> elements;

    AbstractCompositeExpression(List<? extends Expression> elements) {
        this.elements = elements;
    }
}
