package com.jiganaut.bonsai.grammar.impl;

import java.util.Objects;
import java.util.OptionalInt;

import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.Rule;

/**
 * @author Junji Mikami
 *
 */
class DefaultQuantifierRule<T> implements QuantifierRule<T> {

    private final Rule<T> rule;
    private final int minCount;
    private final OptionalInt maxCount;

    DefaultQuantifierRule(Rule<T> rule, int minCount) {
        assert rule != null;
        assert minCount >= 0;
        this.rule = rule;
        this.minCount = minCount;
        this.maxCount = OptionalInt.empty();
    }

    DefaultQuantifierRule(Rule<T> rule, int minCount, int maxCount) {
        assert rule != null;
        assert minCount >= 0;
        assert minCount <= maxCount;
        this.rule = rule;
        this.minCount = minCount;
        this.maxCount = OptionalInt.of(maxCount);
    }

    @Override
    public int getMinCount() {
        return minCount;
    }

    @Override
    public OptionalInt getMaxCount() {
        return maxCount;
    }

    @Override
    public Rule<T> getRule() {
        return rule;
    }

    @Override
    public String toString() {
        var string = rule.toString();
        if (rule.getKind().isComposite()) {
            string = string.isEmpty() ? "()" : "( %s )".formatted(string);
        }
        if (minCount == 0 && maxCount.isEmpty()) {
            return "%s*".formatted(string);
        }
        if (minCount == 1 && maxCount.isEmpty()) {
            return "%s+".formatted(string);
        }
        if (maxCount.isEmpty()) {
            return "%d*%s".formatted(minCount, string);
        }
        if (minCount == 0 && maxCount.getAsInt() == 1) {
            return "%s?".formatted(string);
        }
        if (minCount == maxCount.getAsInt()) {
            return "%d%s".formatted(minCount, string);
        }
        return "%d*%d%s".formatted(minCount, maxCount.getAsInt(), string);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuantifierRule<?> other) {
            return this.getKind() == other.getKind()
                    && this.rule.equals(other.getRule())
                    && this.minCount == other.getMinCount()
                    && this.maxCount.equals(other.getMaxCount());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), rule, minCount, maxCount);
    }

}
