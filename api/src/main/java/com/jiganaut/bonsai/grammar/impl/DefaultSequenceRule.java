package com.jiganaut.bonsai.grammar.impl;

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
            builders.add(() -> Objects.requireNonNull(builder.build(), Message.NULL_PARAMETER.format()));
            return this;
        }

        @Override
        public SequenceRule build() {
            checkForBuild();
            var elements = builders.stream()
                    .map(e -> e.get())
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
