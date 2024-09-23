package com.jiganaut.bonsai.grammar;

import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.impl.GrammarProviders;

/**
 * @author Junji Mikami
 *
 */
public interface PatternRule extends Rule {

    public static interface Builder extends Rule.Builder, Quantifiable {
        @Override
        public PatternRule build();
    }

    public static Builder builder(String regex) {
        return GrammarProviders.provider().createPatternBuilder(regex);
    }

    public static Builder builder(Pattern pattern) {
        return GrammarProviders.provider().createPatternBuilder(pattern);
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
