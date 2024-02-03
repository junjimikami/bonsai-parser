package com.unitedjiga.common.parsing.grammar.impl;

import java.util.ArrayList;
import java.util.List;

import com.unitedjiga.common.parsing.grammar.Rule;

abstract class AbstractCompositeRule extends AbstractRule {
    static abstract class Builder extends QuantifiableBuilder {
        protected final List<Rule.Builder> builders = new ArrayList<>();

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

    protected final List<? extends Rule> elements;

    AbstractCompositeRule(List<? extends Rule> elements) {
        this.elements = elements;
    }
}
