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
        public Builder add(Rule.Builder builder) {
            checkParameter(builder);
            builders.add(builder);
            return this;
        }

        @Override
        public SequenceRule build() {
            checkForBuild();
            var elements = builders.stream().map(e -> e.build()).toList();
            return new DefaultSequenceRule(elements);
        }

    }

    private DefaultSequenceRule(List<Rule> elements) {
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
