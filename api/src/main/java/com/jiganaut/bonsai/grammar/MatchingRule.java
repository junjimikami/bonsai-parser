package com.jiganaut.bonsai.grammar;

import java.util.Objects;

import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.Token;

/**
 * @author Junji Mikami
 *
 */
public interface MatchingRule extends Quantifiable, Skippable {

    @Override
    public default Kind getKind() {
        return Kind.MATCH;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        return visitor.visitMatch(this, p);
    }

    public boolean test(String name, String value);

    public default boolean test(Token token) {
        if (token == null) {
            return false;
        }
        return test(token.getName(), token.getValue());
    }

}
