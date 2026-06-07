package com.jiganaut.bonsai.grammar.impl;

import java.util.Objects;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.EmptyRule;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.SkipRule;
import com.jiganaut.bonsai.grammar.spi.GrammarProvider;
import com.jiganaut.bonsai.impl.Message;

/**
 *
 * @author Junji Mikami
 */
public final class DefaultGrammarProvider extends GrammarProvider {

    private static EmptyRule<?> EMPTY = new EmptyRule<>() {

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof EmptyRule<?> other) {
                return this.getKind() == other.getKind();
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return Objects.hash(Rule.Kind.EMPTY);
        }

        @Override
        public String toString() {
            return "ε";
        }

    };

    @Override
    public <T> Grammar.Builder<T> createGrammarBuilder(String startSymbol) {
        return new DefaultGrammar.Builder<>(startSymbol);
    }

    @Override
    public <T> SequenceRule.Builder<T> createSequenceBuilder() {
        return new DefaultSequenceRule.Builder<>();
    }

    @Override
    public <T> ChoiceRule.Builder<T> createChoiceBuilder() {
        return new DefaultChoiceRule.Builder<>();
    }

    @Override
    public <T> ReferenceRule<T> createReference(String reference) {
        Objects.requireNonNull(reference, () -> Message.VALIDATION_PARAMETER_NULL.format("reference"));
        return new DefaultReferenceRule<>(reference);
    }

    @Override
    public <T> QuantifierRule<T> createQuantifier(Rule<T> rule, int times) {
        Objects.requireNonNull(rule, () -> Message.VALIDATION_PARAMETER_NULL.format("rule"));
        if (times < 0) {
            throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("times", 0, times));
        }
        return new DefaultQuantifierRule<>(rule, times);
    }

    @Override
    public <T> QuantifierRule<T> createQuantifier(Rule<T> rule, int from, int to) {
        Objects.requireNonNull(rule, () -> Message.VALIDATION_PARAMETER_NULL.format("rule"));
        if (from < 0) {
            throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("from", 0, from));
        }
        if (to < from) {
            throw new IllegalArgumentException(Message.VALIDATION_RANGE_INVALID.format("from", from, "to", to));
        }
        return new DefaultQuantifierRule<>(rule, from, to);
    }

    @Override
    public <T> SkipRule<T> createSkip(Rule<T> rule) {
        Objects.requireNonNull(rule, () -> Message.VALIDATION_PARAMETER_NULL.format("rule"));
        return new DefaultSkipRule<>(rule);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> EmptyRule<T> createEmpty() {
        return (EmptyRule<T>) EMPTY;
    }

}
