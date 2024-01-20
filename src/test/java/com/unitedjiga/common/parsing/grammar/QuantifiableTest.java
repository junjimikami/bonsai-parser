package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

interface QuantifiableTest {
    Quantifiable builder();

    @Test
    @DisplayName("exactly(int) [Negative parameter]")
    default void exactlyInCaseNegative() throws Exception {
        var builder = builder();

        assertThrows(IllegalArgumentException.class, () -> builder.exactly(-1))
                .printStackTrace();
    }

    @Test
    @DisplayName("atLeast(int) [Negative parameter]")
    default void atLeastInCaseNegative() throws Exception {
        var builder = builder();

        assertThrows(IllegalArgumentException.class, () -> builder.atLeast(-1))
                .printStackTrace();
    }

    @Test
    @DisplayName("range(int, int) [Negative parameter]")
    default void rangeInCaseNegative() throws Exception {
        var builder = builder();

        assertThrows(IllegalArgumentException.class, () -> builder.range(-1, 0))
                .printStackTrace();
    }

    @ParameterizedTest
    @CsvSource({
            "0,-1", "1,0", "2,1"
    })
    @DisplayName("range(int, int) [Upper limit is lower than lower limit]")
    default void rangeInCaseInvalidUpperLimit(int min, int max) throws Exception {
        var builder = builder();

        assertThrows(IllegalArgumentException.class, () -> builder.range(min, max))
                .printStackTrace();
    }

    @Test
    @DisplayName("exactly(int)")
    default void exactly() throws Exception {
        var builder = builder();
        var quantifier = builder.exactly(0);

        assertNotEquals(builder, quantifier);
        assertInstanceOf(QuantifierExpression.Builder.class, quantifier);
    }

    @Test
    @DisplayName("atLeast(int)")
    default void atLeast() throws Exception {
        var builder = builder();
        var quantifier = builder.atLeast(0);

        assertNotEquals(builder, quantifier);
        assertInstanceOf(QuantifierExpression.Builder.class, quantifier);
    }

    @Test
    @DisplayName("range(int, int)")
    default void range() throws Exception {
        var builder = builder();
        var quantifier = builder.range(0, 0);

        assertNotEquals(builder, quantifier);
        assertInstanceOf(QuantifierExpression.Builder.class, quantifier);
    }

    @Test
    @DisplayName("opt()")
    default void opt() throws Exception {
        var builder = builder();
        var quantifier = builder.opt();

        assertNotEquals(builder, quantifier);
        assertInstanceOf(QuantifierExpression.Builder.class, quantifier);
    }

    @Test
    @DisplayName("zeroOrMore()")
    default void zeroOrMore() throws Exception {
        var builder = builder();
        var quantifier = builder.zeroOrMore();

        assertNotEquals(builder, quantifier);
        assertInstanceOf(QuantifierExpression.Builder.class, quantifier);
    }

    @Test
    @DisplayName("oneOrMore()")
    default void oneOrMore() throws Exception {
        var builder = builder();
        var quantifier = builder.oneOrMore();

        assertNotEquals(builder, quantifier);
        assertInstanceOf(QuantifierExpression.Builder.class, quantifier);
    }

    @Test
    @DisplayName("exactly(int) [Post-build operation]")
    default void exactlyInCasePostBuild() throws Exception {
        var builder = builder();
        builder.build(Stubs.DUMMY_PRODUCTION_SET);

        assertThrows(IllegalStateException.class, () -> builder.exactly(0))
                .printStackTrace();
    }

    @Test
    @DisplayName("atLeast(int) [Post-build operation]")
    default void atLeastInCasePostBuild() throws Exception {
        var builder = builder();
        builder.build(Stubs.DUMMY_PRODUCTION_SET);

        assertThrows(IllegalStateException.class, () -> builder.atLeast(0))
                .printStackTrace();
    }

    @Test
    @DisplayName("range(int, int) [Post-build operation]")
    default void rangeInCasePostBuild() throws Exception {
        var builder = builder();
        builder.build(Stubs.DUMMY_PRODUCTION_SET);

        assertThrows(IllegalStateException.class, () -> builder.range(0, 0))
                .printStackTrace();
    }

    @Test
    @DisplayName("opt() [Post-build operation]")
    default void optInCasePostBuild() throws Exception {
        var builder = builder();
        builder.build(Stubs.DUMMY_PRODUCTION_SET);

        assertThrows(IllegalStateException.class, () -> builder.opt())
                .printStackTrace();
    }

    @Test
    @DisplayName("zeroOrMore() [Post-build operation]")
    default void zeroOrMoreInCasePostBuild() throws Exception {
        var builder = builder();
        builder.build(Stubs.DUMMY_PRODUCTION_SET);

        assertThrows(IllegalStateException.class, () -> builder.zeroOrMore())
                .printStackTrace();
    }

    @Test
    @DisplayName("oneOrMore() [Post-build operation]")
    default void oneOrMoreInCasePostBuild() throws Exception {
        var builder = builder();
        builder.build(Stubs.DUMMY_PRODUCTION_SET);

        assertThrows(IllegalStateException.class, () -> builder.oneOrMore())
                .printStackTrace();
    }

}
