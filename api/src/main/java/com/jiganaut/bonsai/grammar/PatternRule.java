package com.jiganaut.bonsai.grammar;

import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

/**
 * @author Junji Mikami
 *
 */
public interface PatternRule extends Rule, Quantifiable, Skippable {

    public static PatternRule of(String regex) {
        return GrammarProvider.load().createPattern(regex);
    }

    public static PatternRule of(Pattern pattern) {
        return GrammarProvider.load().createPattern(pattern);
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
