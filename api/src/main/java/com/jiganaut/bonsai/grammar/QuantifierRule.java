package com.jiganaut.bonsai.grammar;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Stream;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;
import com.jiganaut.bonsai.impl.Message;

/**
 * @author Junji Mikami
 *
 */
public interface QuantifierRule extends Rule {

    public static interface Builder extends Rule.Builder {

        @Override
        public QuantifierRule build();

    }

    public static QuantifierRule of(Rule rule, int times) {
        return GrammarProvider.load().createQuantifier(rule, times);
    }

    public static QuantifierRule of(Rule rule, int from, int to) {
        return GrammarProvider.load().createQuantifier(rule, from, to);
    }

    @Override
    public default Kind getKind() {
        return Kind.QUANTIFIER;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        return visitor.visitQuantifier(this, p);
    }

    public int getMinCount();
    public OptionalInt getMaxCount();
    public Rule getRule();
    public default Stream<Rule> stream() {
        var stream = Stream.generate(this::getRule);
        if (getMaxCount().isPresent()) {
            return stream.limit(getMaxCount().getAsInt());
        }
        return stream;
    }
}
