package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.OptionalInt;

import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

class QuantifierRuleTest implements RuleTest {

    @Override
    public QuantifierRule createTarget() {
        return PatternRule.of("").opt();
    }

    QuantifierRule createTargetQuantifierRule() {
        return createTarget();
    }

    int expectedMinCount() {
        return 0;
    }

    OptionalInt expectedMaxCount() {
        return OptionalInt.of(1);
    }

    Rule expectedRule() {
        return PatternRule.of("");
    }

    @Override
    public Kind expectedKind() {
        return Kind.QUANTIFIER;
    }

    @Override
    public RuleVisitor<Object[], String> visitor() {
        return new TestRuleVisitor<Object[], String>() {
            @Override
            public Object[] visitQuantifier(QuantifierRule quantifier, String p) {
                return new Object[] { quantifier, p };
            }
        };
    }

    @Test
    void getMinCount() throws Exception {
        var rule = createTargetQuantifierRule();
        assertEquals(expectedMinCount(), rule.getMinCount());
    }

    @Test
    void getMaxCount() throws Exception {
        var rule = createTargetQuantifierRule();
        assertEquals(expectedMaxCount().getAsInt(), rule.getMaxCount().getAsInt());
    }

    @Test
    void getRule() throws Exception {
        var rule = createTargetQuantifierRule();
        assertEquals(expectedRule().getKind(), rule.getRule().getKind()); //TODO
    }

    @Test
    void stream() throws Exception {
        var rule = createTargetQuantifierRule();
        assertEquals(expectedRule().getKind(), rule.stream().findFirst().get().getKind());
    }

//    @Test
//    @DisplayName("opt()")
//    default void opt() throws Exception {
//        var quantifier = builder().opt();
//
//        assertEquals(0, quantifier.getMinCount());
//        assertEquals(1, quantifier.getMaxCount().getAsInt());
//    }
//
//    @Test
//    @DisplayName("zeroOrMore()")
//    default void zeroOrMore() throws Exception {
//        var quantifier = builder().zeroOrMore();
//
//        assertEquals(0, quantifier.getMinCount());
//        assertEquals(true, quantifier.getMaxCount().isEmpty());
//    }
//
//    @Test
//    @DisplayName("oneOrMore()")
//    default void oneOrMore() throws Exception {
//        var quantifier = builder().oneOrMore();
//
//        assertEquals(1, quantifier.getMinCount());
//        assertEquals(true, quantifier.getMaxCount().isEmpty());
//    }
//
//    @Test
//    @DisplayName("exactly()")
//    default void exactly() throws Exception {
//        var quantifier = builder().exactly(0);
//
//        assertEquals(0, quantifier.getMinCount());
//        assertEquals(0, quantifier.getMaxCount().getAsInt());
//    }
//
//    @ParameterizedTest
//    @ValueSource(ints = { 0, 1, 2 })
//    @DisplayName("atLeast(int)")
//    default void atLeast(int times) throws Exception {
//        var quantifier = builder().atLeast(times);
//
//        assertEquals(times, quantifier.getMinCount());
//        assertEquals(true, quantifier.getMaxCount().isEmpty());
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "0,0", "0,1", "0,2",
//            "1,1", "1,2",
//    })
//    @DisplayName("range()")
//    default void range(int min, int max) throws Exception {
//        var quantifier = builder().range(min, max);
//
//        assertEquals(min, quantifier.getMinCount());
//        assertEquals(max, quantifier.getMaxCount().getAsInt());
//    }

}
