package com.jiganaut.bonsai.grammar.impl;

import java.util.Objects;

import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SkipRule;

/**
 * @author Junji Mikami
 *
 */
class DefaultSkipRule<T> implements SkipRule<T> {

    private final Rule<T> rule;

    DefaultSkipRule(Rule<T> rule) {
        assert rule != null;
        this.rule = rule;
    }

    @Override
    public Rule<T> getRule() {
        return rule;
    }

    @Override
    public String toString() {
        var string = rule.toString();
        return string.isEmpty() ? "skip()" : "skip( %s )".formatted(string);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SkipRule<?> other) {
            return this.getKind() == other.getKind()
                    && this.rule.equals(other.getRule());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), rule);
    }

}
