package com.unitedjiga.common.parsing.grammar;

import com.unitedjiga.common.parsing.grammar.QuantifierExpression.Builder;

public interface Quantifiable {

    public default QuantifierExpression.Builder opt() {
        return range(0, 1);
    }
    public default QuantifierExpression.Builder zeroOrMore() {
        return atLeast(0);
    }
    public default QuantifierExpression.Builder oneOrMore() {
        return atLeast(1);
    }
    public default QuantifierExpression.Builder exactly(int times) {
        return range(times, times);
    }
    public QuantifierExpression.Builder atLeast(int times);
    public QuantifierExpression.Builder range(int from, int to);
}
