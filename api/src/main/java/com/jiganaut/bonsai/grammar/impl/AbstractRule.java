package com.jiganaut.bonsai.grammar.impl;

import com.jiganaut.bonsai.grammar.Quantifiable;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.Rule;

/**
 *
 * @author Junji Mikami
 */
abstract class AbstractRule implements Rule {

    static abstract class Builder extends BaseBuilder implements Rule.Builder {
    }

    static abstract class QuantifiableBuilder extends Builder implements Quantifiable {
        protected void checkForQuantifiable() {
            check();
        }

        @Override
        public QuantifierRule.Builder atLeast(int times) {
            checkForQuantifiable();
            if (times < 0) {
                throw new IllegalArgumentException(Message.NEGATIVE_PARAMETER.format());
            }
            return new DefaultQuantifierRule.Builder(this, times);
        }

        @Override
        public QuantifierRule.Builder range(int from, int to) {
            checkForQuantifiable();
            if (from < 0) {
                throw new IllegalArgumentException(Message.NEGATIVE_PARAMETER.format());
            }
            if (to < from) {
                throw new IllegalArgumentException(Message.INVALID_MAX_COUNT.format());
            }
            return new DefaultQuantifierRule.Builder(this, from, to);
        }
    }

}
