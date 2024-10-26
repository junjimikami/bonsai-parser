package com.jiganaut.bonsai.grammar.impl;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Stream;

import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.Rule;

/**
 * @author Junji Mikami
 *
 */
class DefaultQuantifierRule extends AbstractRule implements QuantifierRule {

    private final Rule rule;
    private final int minCount;
    private final OptionalInt maxCount;

    DefaultQuantifierRule(Rule rule, int minCount) {
        assert rule != null;
        assert minCount >= 0;
        this.rule = rule;
        this.minCount = minCount;
        this.maxCount = OptionalInt.empty();
    }

    DefaultQuantifierRule(Rule rule, int minCount, int maxCount) {
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
    public Rule getRule() {
        return rule;
    }

    @Override
    public Stream<Rule> stream() {
        var stream = Stream.generate(() -> rule);
        if (maxCount.isPresent()) {
            return stream.limit(maxCount.getAsInt());
        }
        return stream;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        if (minCount == 0 && maxCount.orElse(0) == 1) {
            sb.append("[");
            sb.append(rule);
            sb.append("]");
            return sb.toString();
        }
        if (minCount == 0 && maxCount.isEmpty()) {
            sb.append("{");
            sb.append(rule);
            sb.append("}");
            return sb.toString();
        }
        sb.append(minCount);
        if (maxCount.isEmpty()) {
            sb.append("*");
        } else if (minCount != maxCount.getAsInt()) {
            sb.append("*");
            sb.append(maxCount.getAsInt());
        }
        sb.append(rule);
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuantifierRule r) {
            return this.getKind() == r.getKind()
                    && this.rule.equals(r.getRule())
                    && this.minCount == r.getMinCount()
                    && this.maxCount.equals(r.getMaxCount());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), rule, minCount, maxCount);
    }

}
