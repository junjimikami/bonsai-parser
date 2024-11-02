package com.jiganaut.bonsai.grammar.impl;

import com.jiganaut.bonsai.grammar.Quantifiable;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.Message;

/**
 *
 * @author Junji Mikami
 */
interface DefaultQuantifiableRule extends Rule, Quantifiable {

    interface Builder extends Rule.Builder, Quantifiable {

        @Override
        public default QuantifierRule atLeast(int times) {
            checkParameter(times);
            return new DefaultQuantifierRule(build(), times);
        }

        @Override
        public default QuantifierRule range(int from, int to) {
            checkParameter(from, to);
            return new DefaultQuantifierRule(build(), from, to);
        }
    }

    @Override
    public default QuantifierRule atLeast(int times) {
        checkParameter(times);
        return new DefaultQuantifierRule(this, times);
    }

    @Override
    public default QuantifierRule range(int from, int to) {
        checkParameter(from, to);
        return new DefaultQuantifierRule(this, from, to);
    }

    private static void checkParameter(int times) {
        if (times < 0) {
            throw new IllegalArgumentException(Message.NEGATIVE_PARAMETER.format());
        }
    }

    private static void checkParameter(int from, int to) {
        checkParameter(from);
        if (to < from) {
            throw new IllegalArgumentException(Message.INVALID_MAX_COUNT.format());
        }
    }
}
