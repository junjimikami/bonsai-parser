package com.jiganaut.bonsai.grammar.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.jiganaut.bonsai.grammar.Quantifiable;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.Rule;

abstract class AbstractCompositeRule extends AbstractRule implements DefaultQuantifiableRule {
    static abstract class Builder extends AbstractRule.Builder implements Quantifiable {
        protected final List<Supplier<Rule>> builders = new ArrayList<>();

        @Override
        public QuantifierRule atLeast(int times) {
            if (times < 0) {
                throw new IllegalArgumentException(Message.NEGATIVE_PARAMETER.format());
            }
            return new DefaultQuantifierRule(build(), times);
        }

        @Override
        public QuantifierRule range(int from, int to) {
            if (from < 0) {
                throw new IllegalArgumentException(Message.NEGATIVE_PARAMETER.format());
            }
            if (to < from) {
                throw new IllegalArgumentException(Message.INVALID_MAX_COUNT.format());
            }
            return new DefaultQuantifierRule(build(), from, to);
        }

    }

    protected final List<Rule> elements;

    AbstractCompositeRule(List<Rule> elements) {
        this.elements = Collections.unmodifiableList(elements);
    }
}
