package com.jiganaut.bonsai.grammar.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;

/**
 *
 * @author Junji Mikami
 */
class DefaultSequenceRule extends AbstractCompositeRule implements SequenceRule {
    static class Builder extends AbstractCompositeRule.Builder implements SequenceRule.Builder {

        Builder() {
        }

        @Override
        public Builder add(Rule rule) {
            checkParameter(rule);
            builders.add(() -> rule);
            return this;
        }

        @Override
        public Builder add(Rule.Builder builder) {
            checkParameter(builder);
            builders.add(builder::build);
            return this;
        }

        @Override
        public SequenceRule build() {
            checkForBuild();
            var elements = builders.stream().map(e -> e.get()).toList();
            return new DefaultSequenceRule(elements);
        }

    }

    private DefaultSequenceRule(List<? extends Rule> elements) {
        super(elements);
    }

    @Override
    public List<? extends Rule> getRules() {
        return elements;
    }

    @Override
    public String toString() {
        return elements.stream()
                .map(e -> {
                    if (e.getKind().isComposite()) {
                        return "(%s)".formatted(e);
                    }
                    return e.toString();
                })
                .collect(Collectors.joining(", "));
    }
}
