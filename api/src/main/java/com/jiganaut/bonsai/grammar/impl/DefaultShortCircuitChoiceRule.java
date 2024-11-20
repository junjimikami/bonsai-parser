package com.jiganaut.bonsai.grammar.impl;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.ShortCircuitChoiceRule;

/**
 * 
 * @author Junji Mikami
 *
 */
class DefaultShortCircuitChoiceRule extends DefaultChoiceRule implements ShortCircuitChoiceRule {
    static class Builder extends DefaultChoiceRule.Builder implements ShortCircuitChoiceRule.Builder {

        @Override
        public Builder add(Rule rule) {
            super.add(rule);
            return this;
        }

        @Override
        public Builder add(Rule.Builder builder) {
            super.add(builder);
            return this;
        }

        @Override
        public Builder addEmpty() {
            super.addEmpty();
            return this;
        }

        @Override
        public ShortCircuitChoiceRule build() {
            checkForBuild();
            var elements = builders.stream()
                    .map(e -> e.get())
                    .<Set<Rule>>collect(LinkedHashSet<Rule>::new, Set::add, Set::addAll);
            elements = Collections.unmodifiableSet(elements);
            return new DefaultShortCircuitChoiceRule(elements);
        }

    }

    DefaultShortCircuitChoiceRule(Set<Rule> elements) {
        super(elements);
    }

    @Override
    public Set<? extends Rule> getChoices() {
        return elements;
    }

    @Override
    public String toString() {
        return elements.stream()
                .map(e -> {
                    try {
                        if (e.getKind().isComposite()) {
                            return "(%s)".formatted(e);
                        }
                        return e.toString();
                    } catch (Exception ex) {
                        return "?";
                    }
                })
                .collect(Collectors.joining(" / "));
    }

}
