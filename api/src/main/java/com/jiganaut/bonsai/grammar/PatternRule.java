package com.jiganaut.bonsai.grammar;

import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.impl.GrammarProviders;

/**
 * @author Junji Mikami
 *
 */
public interface PatternRule extends Rule, Quantifiable, Skippable {

    public static PatternRule of(String regex) {
        return GrammarProviders.provider().createPattern(regex);
    }

    public static PatternRule of(Pattern pattern) {
        return GrammarProviders.provider().createPattern(pattern);
    }

    @Override
    public default Kind getKind() {
        return Kind.PATTERN;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        return visitor.visitPattern(this, p);
    }

    public Pattern getPattern();
}
