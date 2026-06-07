package com.jiganaut.bonsai.grammar.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.impl.Message;

/**
 *
 * @author Junji Mikami
 */
class DefaultSequenceRule<T> extends CompositeRule<T, List<Rule<T>>> implements SequenceRule<T> {

    /**
     *
     */
    static class Builder<T> extends CompositeRule.Builder<T> implements SequenceRule.Builder<T> {

        Builder() {
            super(new ArrayList<>());
        }

        @Override
        public Builder<T> add(Rule.Builder<T> builder) {
            check();
            Objects.requireNonNull(builder, () -> Message.VALIDATION_PARAMETER_NULL.format("builder"));
            builders.add(builder);
            return this;
        }

        @Override
        public Builder<T> addAll(SequenceRule.Builder<T> builder) {
            check();
            Objects.requireNonNull(builder, () -> Message.VALIDATION_PARAMETER_NULL.format("builder"));
            builder.forEach(this::add);
            return this;
        }

        @Override
        public SequenceRule<T> build() {
            checkForBuild();
            var elements = builders.stream()
                    .map(Rule.Builder::build)
                    .filter(Objects::nonNull)
                    .toList();
            return new DefaultSequenceRule<>(elements);
        }

    }

    private DefaultSequenceRule(List<Rule<T>> elements) {
        super(elements);
    }

    @Override
    public List<? extends Rule<T>> getRules() {
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
                .collect(Collectors.joining(" "));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SequenceRule<?> other) {
            return this.getKind() == other.getKind()
                    && this.elements.equals(other.getRules());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), elements);
    }

}
