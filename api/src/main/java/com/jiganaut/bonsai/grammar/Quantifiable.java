package com.jiganaut.bonsai.grammar;

public interface Quantifiable extends Rule.Builder {

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
    public QuantifierRule.Builder atLeast(int times);
    public QuantifierRule.Builder range(int from, int to);
}
