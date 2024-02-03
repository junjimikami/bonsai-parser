package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.unitedjiga.common.parsing.grammar.Rule.Kind;

interface QuantifierRuleTest extends RuleTest {

    interface BuilderTest extends RuleTest.BulderTest {
        QuantifierRule.Builder builder();
    }

    Quantifiable builder();

    @Override
    default Rule build() {
        return builder().opt().build(Stubs.DUMMY_PRODUCTION_SET);
    }

    @Override
    default Kind kind() {
        return Kind.QUANTIFIER;
    }

    @Override
    default RuleVisitor<Object[], String> visitor() {
        return new TestRuleVisitor<Object[], String>() {
            @Override
            public Object[] visitQuantifier(QuantifierRule quantifier, String p) {
                return new Object[] { quantifier, p };
            }
        };
    }

    @Test
    @DisplayName("opt()")
    default void opt() throws Exception {
        var quantifier = builder().opt().build(Stubs.DUMMY_PRODUCTION_SET);

        assertEquals(0, quantifier.getMinCount());
        assertEquals(1, quantifier.getMaxCount().getAsInt());
    }

    @Test
    @DisplayName("zeroOrMore()")
    default void zeroOrMore() throws Exception {
        var quantifier = builder().zeroOrMore().build(Stubs.DUMMY_PRODUCTION_SET);

        assertEquals(0, quantifier.getMinCount());
        assertEquals(true, quantifier.getMaxCount().isEmpty());
    }

    @Test
    @DisplayName("oneOrMore()")
    default void oneOrMore() throws Exception {
        var quantifier = builder().oneOrMore().build(Stubs.DUMMY_PRODUCTION_SET);

        assertEquals(1, quantifier.getMinCount());
        assertEquals(true, quantifier.getMaxCount().isEmpty());
    }

    @Test
    @DisplayName("exactly()")
    default void exactly() throws Exception {
        var quantifier = builder().exactly(0).build(Stubs.DUMMY_PRODUCTION_SET);

        assertEquals(0, quantifier.getMinCount());
        assertEquals(0, quantifier.getMaxCount().getAsInt());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    @DisplayName("atLeast(int)")
    default void atLeast(int times) throws Exception {
        var quantifier = builder().atLeast(times).build(Stubs.DUMMY_PRODUCTION_SET);

        assertEquals(times, quantifier.getMinCount());
        assertEquals(true, quantifier.getMaxCount().isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "0,0", "0,1", "0,2",
            "1,1", "1,2",
    })
    @DisplayName("range()")
    default void range(int min, int max) throws Exception {
        var quantifier = builder().range(min, max).build(Stubs.DUMMY_PRODUCTION_SET);

        assertEquals(min, quantifier.getMinCount());
        assertEquals(max, quantifier.getMaxCount().getAsInt());
    }

}
