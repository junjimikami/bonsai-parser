package com.jiganaut.bonsai.grammar.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.jiganaut.bonsai.grammar.Rule;

abstract class AbstractCompositeRule<E extends Collection<Rule>> extends AbstractRule implements DefaultQuantifiableRule {
    static abstract class Builder extends AbstractRule.Builder implements DefaultQuantifiableRule.Builder {
        protected final List<Supplier<Rule>> builders = new ArrayList<>();
    }

    protected final E elements;

    AbstractCompositeRule(E elements) {
        this.elements = elements;
    }
}
