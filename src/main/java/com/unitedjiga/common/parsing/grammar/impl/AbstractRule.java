/*
 * The MIT License
 *
 * Copyright 2020 Junji Mikami.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.unitedjiga.common.parsing.grammar.impl;

import com.unitedjiga.common.parsing.grammar.Rule;
import com.unitedjiga.common.parsing.grammar.Quantifiable;
import com.unitedjiga.common.parsing.grammar.QuantifierRule;

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
                throw new IllegalArgumentException(Message.INVALID_UPPER_LIMIT.format());
            }
            return new DefaultQuantifierRule.Builder(this, from, to);
        }
    }

}
