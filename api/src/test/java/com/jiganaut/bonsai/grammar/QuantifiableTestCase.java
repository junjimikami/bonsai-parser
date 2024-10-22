package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

interface QuantifiableTestCase extends TestCase {
    Quantifiable createTarget();

    default Quantifiable createTargetQuantifiable() {
        return createTarget();
    }

    @Test
    @DisplayName("exactly(int) [Negative parameter]")
    default void exactlyInCaseOfNegativeParameter() throws Exception {
        var builder = createTargetQuantifiable();

        assertThrows(IllegalArgumentException.class, () -> builder.exactly(-1));
    }

    @Test
    @DisplayName("atLeast(int) [Negative parameter]")
    default void atLeastInCaseOfNegativeParameter() throws Exception {
        var builder = createTargetQuantifiable();

        assertThrows(IllegalArgumentException.class, () -> builder.atLeast(-1));
    }

    @Test
    @DisplayName("range(int, int) [Negative parameter]")
    default void rangeInCaseOfNegativeParameter() throws Exception {
        var builder = createTargetQuantifiable();

        assertThrows(IllegalArgumentException.class, () -> builder.range(-1, 0));
    }

    @ParameterizedTest
    @CsvSource({
            "0,-1", "1,0", "2,1"
    })
    @DisplayName("range(int, int) [Max count < Min count]")
    default void rangeInCaseOfInvalidMaxCount(int min, int max) throws Exception {
        var builder = createTargetQuantifiable();

        assertThrows(IllegalArgumentException.class, () -> builder.range(min, max));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    @DisplayName("exactly(int)")
    default void exactly(int times) throws Exception {
        var builder = createTargetQuantifiable();
        var quantifier = builder.exactly(times);

        assertEquals(times, quantifier.getMinCount());
        assertEquals(times, quantifier.getMaxCount().getAsInt());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    @DisplayName("atLeast(int)")
    default void atLeast(int times) throws Exception {
        var builder = createTargetQuantifiable();
        var quantifier = builder.atLeast(times);

        assertEquals(times, quantifier.getMinCount());
        assertEquals(true, quantifier.getMaxCount().isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "0,0", "0,1", "0,2",
            "1,1", "1,2",
    })
    @DisplayName("range(int, int)")
    default void range(int min, int max) throws Exception {
        var builder = createTargetQuantifiable();
        var quantifier = builder.range(min, max);

        assertEquals(min, quantifier.getMinCount());
        assertEquals(max, quantifier.getMaxCount().getAsInt());
    }

    @Test
    @DisplayName("opt()")
    default void opt() throws Exception {
        var builder = createTargetQuantifiable();
        var quantifier = builder.opt();

        assertEquals(0, quantifier.getMinCount());
        assertEquals(1, quantifier.getMaxCount().getAsInt());
    }

    @Test
    @DisplayName("zeroOrMore()")
    default void zeroOrMore() throws Exception {
        var builder = createTargetQuantifiable();
        var quantifier = builder.zeroOrMore();

        assertEquals(0, quantifier.getMinCount());
        assertEquals(true, quantifier.getMaxCount().isEmpty());
    }

    @Test
    @DisplayName("oneOrMore()")
    default void oneOrMore() throws Exception {
        var builder = createTargetQuantifiable();
        var quantifier = builder.oneOrMore();

        assertEquals(1, quantifier.getMinCount());
        assertEquals(true, quantifier.getMaxCount().isEmpty());
    }

}
