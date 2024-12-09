package com.jiganaut.bonsai.grammar.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.Message;

/**
 * 
 * @author Junji Mikami
 */
abstract class AbstractCompositeRule<E extends Collection<Rule>> extends AbstractRule
        implements DefaultQuantifiableRule {
    /**
     * 
     * @author Junji Mikami
     */
    static abstract class Builder extends AbstractRule.Builder implements DefaultQuantifiableRule.Builder {
        final List<Supplier<Rule>> suppliers = new ArrayList<>();

        public Builder add(Rule rule) {
            checkParameter(rule);
            suppliers.add(() -> rule);
            return this;
        }

        public Builder add(Rule.Builder builder) {
            checkParameter(builder);
            suppliers.add(() -> Objects.requireNonNull(builder.build(), Message.NULL_BUILD.format()));
            return this;
        }

    }

    final E elements;

    AbstractCompositeRule(E elements) {
        this.elements = elements;
    }
}
