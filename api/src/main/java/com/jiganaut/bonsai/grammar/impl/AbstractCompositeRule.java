package com.jiganaut.bonsai.grammar.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.jiganaut.bonsai.grammar.Rule;

abstract class AbstractCompositeRule extends AbstractRule implements DefaultQuantifiableRule {
    static abstract class Builder extends AbstractRule.Builder implements DefaultQuantifiableRule.Builder {
        protected final List<Supplier<Rule>> builders = new ArrayList<>();
    }

    protected final List<Rule> elements;

    AbstractCompositeRule(List<Rule> elements) {
        this.elements = Collections.unmodifiableList(elements);
    }
}
