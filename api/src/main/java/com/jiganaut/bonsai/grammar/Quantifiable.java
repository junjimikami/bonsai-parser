package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.impl.Message;

/**
 *
 * @author Junji Mikami
 */
public interface Quantifiable extends Rule {

    public static interface Builder extends Rule.Builder {

        public default QuantifierRule.Builder opt() {
            return range(0, 1);
        }

        public default QuantifierRule.Builder zeroOrMore() {
            return atLeast(0);
        }

        public default QuantifierRule.Builder oneOrMore() {
            return atLeast(1);
        }

        public default QuantifierRule.Builder exactly(int times) {
            return range(times, times);
        }

        public default QuantifierRule.Builder atLeast(int times) {
            if (times < 0) {
                throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("times", 0, times));
            }
            return () -> QuantifierRule.of(build(), times);
        }

        public default QuantifierRule.Builder range(int from, int to) {
            if (from < 0) {
                throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("from", 0, from));
            }
            if (to < from) {
                throw new IllegalArgumentException(Message.VALIDATION_RANGE_INVALID.format("from", from, "to", to));
            }
            return () -> QuantifierRule.of(build(), from, to);
        }

    }

    public default QuantifierRule opt() {
        return range(0, 1);
    }

    public default QuantifierRule zeroOrMore() {
        return atLeast(0);
    }

    public default QuantifierRule oneOrMore() {
        return atLeast(1);
    }

    public default QuantifierRule exactly(int times) {
        return range(times, times);
    }

    public default QuantifierRule atLeast(int times) {
        return QuantifierRule.of(this, times);
    }

    public default QuantifierRule range(int from, int to) {
        return QuantifierRule.of(this, from, to);
    }

}
