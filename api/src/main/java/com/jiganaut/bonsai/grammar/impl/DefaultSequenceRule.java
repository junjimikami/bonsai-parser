package com.jiganaut.bonsai.grammar.impl;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;

/**
 *
 * @author Junji Mikami
 */
class DefaultSequenceRule extends AbstractCompositeRule<List<Rule>> implements SequenceRule {
    /**
     * 
     * @author Junji Mikami
     */
    static class Builder extends AbstractCompositeRule.Builder implements SequenceRule.Builder {

        @Override
        public Builder add(Rule rule) {
            return (Builder) super.add(rule);
        }

        @Override
        public Builder add(Rule.Builder builder) {
            return (Builder) super.add(builder);
        }

        @Override
        public SequenceRule build() {
            checkForBuild();
            var elements = suppliers.stream()
                    .map(Supplier::get)
                    .toList();
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
                    try {
                        if (e.getKind().isComposite()) {
                            return "(%s)".formatted(e);
                        }
                        return e.toString();
                    } catch (Exception ex) {
                        return "?";
                    }
                })
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SequenceRule r) {
            return this.getKind() == r.getKind()
                    && this.elements.equals(r.getRules());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), elements);
    }

}
