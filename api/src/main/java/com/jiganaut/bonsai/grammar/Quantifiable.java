package com.jiganaut.bonsai.grammar;

public interface Quantifiable {

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
    public QuantifierRule atLeast(int times);
    public QuantifierRule range(int from, int to);
}
