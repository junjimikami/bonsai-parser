package com.jiganaut.bonsai.grammar.impl;

import java.util.OptionalInt;
import java.util.stream.Stream;

import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.Rule;

/**
 * @author Junji Mikami
 *
 */
class DefaultQuantifierRule extends AbstractRule implements QuantifierRule {
    static class Builder extends AbstractRule.Builder implements QuantifierRule.Builder {
        private final Rule.Builder builder;
        private final int from;
        private final Integer to;

        Builder(Rule.Builder builder, int from) {
            assert builder != null;
            this.builder = builder;
            this.from = from;
            this.to = null;
        }

        Builder(Rule.Builder builder, int from, int to) {
            assert builder != null;
            this.builder = builder;
            this.from = from;
            this.to = to;
        }

        @Override
        public QuantifierRule build() {
            checkForBuild();
            return new DefaultQuantifierRule(builder.build(), from, to);
        }
    }

    private final Rule rule;
    private final int minCount;
    private final OptionalInt maxCount;

    private DefaultQuantifierRule(Rule rule, int minCount, Integer maxCount) {
        assert rule != null;
        this.rule = rule;
        this.minCount = minCount;
        if (maxCount == null) {
            this.maxCount = OptionalInt.empty();
        } else {
            this.maxCount = OptionalInt.of(maxCount);
        }
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
}
