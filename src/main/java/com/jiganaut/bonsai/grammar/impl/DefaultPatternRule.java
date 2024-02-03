package com.jiganaut.bonsai.grammar.impl;

import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.ProductionSet;

/**
 *
 * @author Junji Mikami
 */
class DefaultPatternRule extends AbstractRule implements PatternRule {
    static class Builder extends AbstractRule.QuantifiableBuilder implements PatternRule.Builder {
        private Pattern pattern;

        Builder(String regex) {
            assert regex != null;
            this.pattern = Pattern.compile(regex);
        }

        Builder(Pattern pattern) {
            assert pattern != null;
            this.pattern = pattern;
        }

        @Override
        public PatternRule build(ProductionSet set) {
            checkForBuild();
            return new DefaultPatternRule(pattern);
        }

    }

    private final Pattern pattern;

    private DefaultPatternRule(Pattern pattern) {
        assert pattern != null;
        this.pattern = pattern;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return "\"%s\"".formatted(pattern.pattern());
    }
}
