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
class DefaultChoiceRule<T> extends CompositeRule<T, Set<Rule<T>>> implements ChoiceRule<T> {

    /**
     *
     */
    static class Builder<T> extends CompositeRule.Builder<T> implements ChoiceRule.Builder<T> {

        private boolean shortCircuit = false;

        Builder() {
            super(new LinkedHashSet<>());
        }

        @Override
        public Builder<T> add(Rule.Builder<T> builder) {
            check();
            Objects.requireNonNull(builder, () -> Message.VALIDATION_PARAMETER_NULL.format("builder"));
            builders.add(builder);
            return this;
        }

        @Override
        public Builder<T> addAll(ChoiceRule.Builder<T> builder) {
            check();
            Objects.requireNonNull(builder, () -> Message.VALIDATION_PARAMETER_NULL.format("builder"));
            builder.forEach(this::add);
            return this;
        }

        @Override
        public Builder<T> asShortCircuit() {
            check();
            shortCircuit = true;
            return this;
        }

        @Override
        public ChoiceRule<T> build() {
            checkForBuild();
            var collector = shortCircuit
                    ? Collectors.toCollection(LinkedHashSet<Rule<T>>::new)
                    : Collectors.<Rule<T>>toSet();
            var elements = builders.stream()
                    .map(Rule.Builder::build)
                    .filter(Objects::nonNull)
                    .collect(collector);
            return new DefaultChoiceRule<>(elements, shortCircuit);
        }

    }

    private final boolean shortCircuit;
    private final String delimiter;

    private DefaultChoiceRule(Set<Rule<T>> elements, boolean shortCircuit) {
        super(Collections.unmodifiableSet(elements));
        this.shortCircuit = shortCircuit;
        this.delimiter = shortCircuit ? " / " : " | ";
    }

    @Override
    public Set<? extends Rule<T>> getChoices() {
        return elements;
    }

    @Override
    public String toString() {
        return elements.stream()
                .map(e -> {
                    if (e.getKind().isComposite()) {
                        return "( %s )".formatted(e.toString());
                    }
                    return e.toString();
                })
                .collect(Collectors.joining(delimiter));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChoiceRule<?> other) {
            return this.getKind() == other.getKind()
                    && this.shortCircuit == other.isShortCircuit()
                    && this.elements.equals(other.getChoices());
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

}
