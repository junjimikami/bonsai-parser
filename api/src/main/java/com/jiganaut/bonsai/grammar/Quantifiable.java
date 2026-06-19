package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.impl.Message;

/**
 *
 * @author Junji Mikami
 */
public interface Quantifiable<T> extends Rule<T> {

    public static interface Builder<T> extends Rule.Builder<T> {

        public default QuantifierRule.Builder<T> opt() {
            return range(0, 1);
        }

        public default QuantifierRule.Builder<T> zeroOrMore() {
            return atLeast(0);
        }

        public default QuantifierRule.Builder<T> oneOrMore() {
            return atLeast(1);
        }

        public default QuantifierRule.Builder<T> exactly(int times) {
            return range(times, times);
        }

        public default QuantifierRule.Builder<T> atLeast(int times) {
            if (times < 0) {
                throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("times", 0, times));
            }
            return () -> QuantifierRule.of(build(), times);
        }

        public default QuantifierRule.Builder<T> range(int from, int to) {
            if (from < 0) {
                throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("from", 0, from));
            }
            if (to < from) {
                throw new IllegalArgumentException(Message.VALIDATION_RANGE_INVALID.format("from", from, "to", to));
            }
            return () -> QuantifierRule.of(build(), from, to);
        }

    }

    public default QuantifierRule<T> opt() {
        return range(0, 1);
    }

    public default QuantifierRule<T> zeroOrMore() {
        return atLeast(0);
    }

    public default QuantifierRule<T> oneOrMore() {
        return atLeast(1);
    }

    public default QuantifierRule<T> exactly(int times) {
        return range(times, times);
    }

    public default QuantifierRule<T> atLeast(int times) {
        return QuantifierRule.of(this, times);
    }

    public default QuantifierRule<T> range(int from, int to) {
        return QuantifierRule.of(this, from, to);
    }

}
