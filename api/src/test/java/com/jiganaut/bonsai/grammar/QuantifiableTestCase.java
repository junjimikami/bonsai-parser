package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.jiganaut.bonsai.TestCase;

interface QuantifiableTestCase extends TestCase {
    Quantifiable createTarget();

    @Test
    @DisplayName("exactly(int) [Negative parameter]")
    default void exactlyInCaseOfNegativeParameter(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(IllegalArgumentException.class, () -> target.exactly(-1));
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("atLeast(int) [Negative parameter]")
    default void atLeastInCaseOfNegativeParameter(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(IllegalArgumentException.class, () -> target.atLeast(-1));
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("range(int, int) [Negative parameter]")
    default void rangeInCaseOfNegativeParameter(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(IllegalArgumentException.class, () -> target.range(-1, 0));
        testReporter.publishEntry(ex.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "0,-1", "1,0", "2,1"
    })
    @DisplayName("range(int, int) [Max count < Min count]")
    default void rangeInCaseOfInvalidMaxCount(int min, int max, TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(IllegalArgumentException.class, () -> target.range(min, max));
        testReporter.publishEntry(ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    @DisplayName("exactly(int)")
    default void exactly(int times) throws Exception {
        var target = createTarget();
        var quantifier = target.exactly(times);

        assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
        assertEquals(times, quantifier.getMinCount());
        assertEquals(times, quantifier.getMaxCount().getAsInt());
        assertEquals(times, quantifier.stream().limit(9).count());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    @DisplayName("atLeast(int)")
    default void atLeast(int times) throws Exception {
        var target = createTarget();
        var quantifier = target.atLeast(times);

        assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
        assertEquals(times, quantifier.getMinCount());
        assertEquals(true, quantifier.getMaxCount().isEmpty());
        assertEquals(9, quantifier.stream().limit(9).count());
    }

    @ParameterizedTest
    @CsvSource({
            "0,0", "0,1", "0,2",
            "1,1", "1,2",
    })
    @DisplayName("range(int, int)")
    default void range(int min, int max) throws Exception {
        var target = createTarget();
        var quantifier = target.range(min, max);

        assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
        assertEquals(min, quantifier.getMinCount());
        assertEquals(max, quantifier.getMaxCount().getAsInt());
        assertEquals(max, quantifier.stream().limit(9).count());
    }

    @Test
    @DisplayName("opt()")
    default void opt() throws Exception {
        var target = createTarget();
        var quantifier = target.opt();

        assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
        assertEquals(0, quantifier.getMinCount());
        assertEquals(1, quantifier.getMaxCount().getAsInt());
        assertEquals(1, quantifier.stream().limit(9).count());
    }

    @Test
    @DisplayName("zeroOrMore()")
    default void zeroOrMore() throws Exception {
        var target = createTarget();
        var quantifier = target.zeroOrMore();

        assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
        assertEquals(0, quantifier.getMinCount());
        assertEquals(true, quantifier.getMaxCount().isEmpty());
        assertEquals(9, quantifier.stream().limit(9).count());
    }

    @Test
    @DisplayName("oneOrMore()")
    default void oneOrMore() throws Exception {
        var target = createTarget();
        var quantifier = target.oneOrMore();

        assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
        assertEquals(1, quantifier.getMinCount());
        assertEquals(true, quantifier.getMaxCount().isEmpty());
        assertEquals(9, quantifier.stream().limit(9).count());
    }

}
