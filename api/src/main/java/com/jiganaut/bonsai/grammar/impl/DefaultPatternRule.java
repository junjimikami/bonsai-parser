package com.jiganaut.bonsai.grammar.impl;

import java.util.Objects;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.impl.Message;

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
        return Message.stringEncode(pattern.pattern());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PatternRule r) {
            return this.getKind() == r.getKind()
                    && r.getPattern() != null
                    && this.pattern.pattern().equals(r.getPattern().pattern())
                    && this.pattern.flags() == r.getPattern().flags();
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), pattern.pattern(), pattern.flags());
    }
}
