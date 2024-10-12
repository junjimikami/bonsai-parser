package com.jiganaut.bonsai.grammar.impl;

import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.PatternRule;

/**
 *
 * @author Junji Mikami
 */
class DefaultPatternRule extends AbstractRule implements PatternRule, DefaultQuantifiableRule, DefaultSkippableRule {
    private final Pattern pattern;

    DefaultPatternRule(Pattern pattern) {
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
