package com.jiganaut.bonsai.grammar.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.ProductionSet;
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
        public Builder add(Rule.Builder builder) {
            checkParameter(builder);
            builders.add(builder);
            return this;
        }

        @Override
        public Builder add(String reference) {
            checkParameter(reference);
            builders.add(new DefaultReferenceRule.Builder(reference));
            return this;
        }

        @Override
        public Builder addEmpty() {
            check();
            builders.add(emptyBuilder);
            return this;
        }

        @Override
        public ChoiceRule build(ProductionSet set) {
            checkForBuild();
            var elements = builders.stream().map(e -> e.build(set)).toList();
            return new DefaultChoiceRule(elements);
        }

    }

    private static final Rule.Builder emptyBuilder = new Rule.Builder() {

        @Override
        public Rule build(ProductionSet set) {
            return Rule.EMPTY;
        }
    };

    private DefaultChoiceRule(List<? extends Rule> elements) {
        super(elements);
    }

    @Override
    public List<? extends Rule> getChoices() {
        return elements;
    }

    @Override
    public String toString() {
        return elements.stream()
                .map(Rule::toString)
                .collect(Collectors.joining("|", "(", ")"));
    }
}
