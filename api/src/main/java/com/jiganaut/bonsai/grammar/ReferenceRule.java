package com.jiganaut.bonsai.grammar;

import java.util.Objects;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;
import com.jiganaut.bonsai.impl.Message;

/**
 * @author Junji Mikami
 *
 */
public interface ReferenceRule extends Quantifiable {

    public static ReferenceRule of(String reference) {
        return GrammarProvider.load().createReference(reference);
    }

    @Override
    public default Kind getKind() {
        return Kind.REFERENCE;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        return visitor.visitReference(this, p);
    }

    public default ChoiceRule lookup(Grammar grammar) {
        Objects.requireNonNull(grammar, () -> Message.VALIDATION_PARAMETER_NULL.format("grammar"));
        var builder = grammar.getProductionRules().stream()
                .filter(e -> Objects.equals(getSymbol(), e.getSymbol()))
                .collect(ChoiceRule::builder,
                     ChoiceRule.Builder::add,
                     ChoiceRule.Builder::addAll);
        if (grammar.isShortCircuit()) {
            builder.asShortCircuit();
        }
        return builder.build();
    }

    public String getSymbol();
}
