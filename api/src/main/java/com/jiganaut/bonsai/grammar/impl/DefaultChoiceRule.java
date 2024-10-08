package com.jiganaut.bonsai.grammar.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Rule;

/**
 * 
 * @author Junji Mikami
 *
 */
class DefaultChoiceRule extends AbstractCompositeRule implements ChoiceRule {
    static class Builder extends AbstractCompositeRule.Builder implements ChoiceRule.Builder {

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
        public Builder addEmpty() {
            check();
            builders.add(() -> Rule.EMPTY);
            return this;
        }

        @Override
        public ChoiceRule build() {
            checkForBuild();
            var elements = builders.stream().map(e -> e.get()).toList();
            return new DefaultChoiceRule(elements);
        }

    }

    private DefaultChoiceRule(List<Rule> elements) {
        super(elements);
    }

    @Override
    public List<? extends Rule> getChoices() {
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
                .collect(Collectors.joining(" | "));
    }
}
