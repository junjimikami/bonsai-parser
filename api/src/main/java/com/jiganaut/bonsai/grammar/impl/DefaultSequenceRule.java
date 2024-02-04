package com.jiganaut.bonsai.grammar.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.ProductionSet;
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
        public Builder add(String reference) {
            checkParameter(reference);
            builders.add(new DefaultReferenceRule.Builder(reference));
            return this;
        }

        @Override
        public SequenceRule build(ProductionSet set) {
            checkForBuild();
            var elements = builders.stream().map(e -> e.build(set)).toList();
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
                .map(Rule::toString)
                .collect(Collectors.joining(",", "(", ")"));
    }
}
