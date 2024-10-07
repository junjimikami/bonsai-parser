package com.jiganaut.bonsai.grammar.impl;

import com.jiganaut.bonsai.grammar.Quantifiable;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.Rule;

/**
 *
 * @author Junji Mikami
 */
interface DefaultQuantifiableRule extends Rule, Quantifiable {

    @Override
    public default QuantifierRule atLeast(int times) {
        if (times < 0) {
            throw new IllegalArgumentException(Message.NEGATIVE_PARAMETER.format());
        }
        return new DefaultQuantifierRule(this, times);
    }

    @Override
    public default QuantifierRule range(int from, int to) {
        if (from < 0) {
            throw new IllegalArgumentException(Message.NEGATIVE_PARAMETER.format());
        }
        if (to < from) {
            throw new IllegalArgumentException(Message.INVALID_MAX_COUNT.format());
        }
        return new DefaultQuantifierRule(this, from, to);
    }
}
