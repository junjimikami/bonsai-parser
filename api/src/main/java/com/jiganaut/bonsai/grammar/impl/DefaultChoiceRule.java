package com.jiganaut.bonsai.grammar.impl;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.Message;

/**
 * 
 * @author Junji Mikami
 *
 */
class DefaultChoiceRule extends AbstractCompositeRule<Set<Rule>> implements ChoiceRule {
    static class Builder extends AbstractCompositeRule.Builder implements ChoiceRule.Builder {

        @Override
        public Builder add(Rule rule) {
            checkParameter(rule);
            builders.add(() -> rule);
            return this;
        }

        @Override
        public Builder add(Rule.Builder builder) {
            checkParameter(builder);
            builders.add(() -> Objects.requireNonNull(builder.build(), Message.NULL_PARAMETER.format()));
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
            var elements = builders.stream()
                    .map(e -> e.get())
                    .collect(LinkedHashSet<Rule>::new, Set::add, Set::addAll);
            return new DefaultChoiceRule(elements, false);
        }

    }

    private final boolean shortCircuit;

    DefaultChoiceRule(Set<Rule> elements, boolean shortCircuit) {
        super(Collections.unmodifiableSet(elements));
        this.shortCircuit = shortCircuit;
    }

    @Override
    public Set<? extends Rule> getChoices() {
        return elements;
    }

    @Override
    public String toString() {
        var delimiter = isShortCircuit() ? " / " : " | ";
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
                .collect(Collectors.joining(delimiter));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChoiceRule r) {
            return this.getKind() == r.getKind()
                    && this.shortCircuit == r.isShortCircuit()
                    && this.elements.equals(r.getChoices());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), shortCircuit, elements);
    }

    @Override
    public boolean isShortCircuit() {
        return shortCircuit;
    }

    @Override
    public ChoiceRule shortCircuit() {
        return new DefaultChoiceRule(elements, true);
    }

}
