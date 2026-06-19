package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author Junji Mikami
 */
interface QuantifiableTestCase<T> extends RuleTestCase<T> {

    @Nested
    interface BuilderTestCase<T> extends RuleTestCase.BuilderTestCase<T> {

        @Override
        Quantifiable.Builder<T> createTarget();

        @Override
        Quantifiable<T> expectedRule();

        @SuppressWarnings("exports")
        @Test
        @DisplayName("exactly(times:int) [times < 0]")
        default void exactlyWhenTimesIsNegative(TestReporter testReporter) throws Exception {
            var target = createTarget();

            var ex = assertThrows(IllegalArgumentException.class, () -> target.exactly(-1));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("atLeast(times:int) [times < 0]")
        default void atLeastWhenTimesIsNegative(TestReporter testReporter) throws Exception {
            var target = createTarget();

            var ex = assertThrows(IllegalArgumentException.class, () -> target.atLeast(-1));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("range(min:int, max:int) [min < 0]")
        default void rangeWhenMinIsNegative(TestReporter testReporter) throws Exception {
            var target = createTarget();

            var ex = assertThrows(IllegalArgumentException.class, () -> target.range(-1, 0));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @ParameterizedTest
        @CsvSource({
                "0,-1", "1,0", "2,1"
        })
        @DisplayName("range(min:int, max:int) [max < min]")
        default void rangeWhenMaxIsLessThanMin(int min, int max, TestReporter testReporter) throws Exception {
            var target = createTarget();

            var ex = assertThrows(IllegalArgumentException.class, () -> target.range(min, max));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @ParameterizedTest
        @ValueSource(ints = { 0, 1, 2 })
        @DisplayName("exactly(int)")
        default void exactly(int times) throws Exception {
            var target = createTarget();
            var builder = target.exactly(times);

            assertInstanceOf(QuantifierRule.Builder.class, builder);

            var quantifier = builder.build();

            assertInstanceOf(QuantifierRule.class, quantifier);
            assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
            assertEquals(times, quantifier.getMinCount());
            assertEquals(times, quantifier.getMaxCount().getAsInt());
            assertEquals(times, quantifier.stream().limit(9).count());
            assertEquals(expectedRule(), quantifier.getRule());
        }

        @ParameterizedTest
        @ValueSource(ints = { 0, 1, 2 })
        @DisplayName("atLeast(int)")
        default void atLeast(int times) throws Exception {
            var target = createTarget();
            var builder = target.atLeast(times);

            assertInstanceOf(QuantifierRule.Builder.class, builder);

            var quantifier = builder.build();

            assertInstanceOf(QuantifierRule.class, quantifier);
            assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
            assertEquals(times, quantifier.getMinCount());
            assertEquals(true, quantifier.getMaxCount().isEmpty());
            assertEquals(9, quantifier.stream().limit(9).count());
            assertEquals(expectedRule(), quantifier.getRule());
        }

        @ParameterizedTest
        @CsvSource({
                "0,0", "0,1", "0,2",
                "1,1", "1,2",
        })
        @DisplayName("range(int, int)")
        default void range(int min, int max) throws Exception {
            var target = createTarget();
            var builder = target.range(min, max);

            assertInstanceOf(QuantifierRule.Builder.class, builder);

            var quantifier = builder.build();

            assertInstanceOf(QuantifierRule.class, quantifier);
            assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
            assertEquals(min, quantifier.getMinCount());
            assertEquals(max, quantifier.getMaxCount().getAsInt());
            assertEquals(max, quantifier.stream().limit(9).count());
            assertEquals(expectedRule(), quantifier.getRule());
        }

        @Test
        @DisplayName("opt()")
        default void opt() throws Exception {
            var target = createTarget();
            var builder = target.opt();

            assertInstanceOf(QuantifierRule.Builder.class, builder);

            var quantifier = builder.build();

            assertInstanceOf(QuantifierRule.class, quantifier);
            assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
            assertEquals(0, quantifier.getMinCount());
            assertEquals(1, quantifier.getMaxCount().getAsInt());
            assertEquals(1, quantifier.stream().limit(9).count());
            assertEquals(expectedRule(), quantifier.getRule());
        }

        @Test
        @DisplayName("zeroOrMore()")
        default void zeroOrMore() throws Exception {
            var target = createTarget();
            var builder = target.zeroOrMore();

            assertInstanceOf(QuantifierRule.Builder.class, builder);

            var quantifier = builder.build();

            assertInstanceOf(QuantifierRule.class, quantifier);
            assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
            assertEquals(0, quantifier.getMinCount());
            assertEquals(true, quantifier.getMaxCount().isEmpty());
            assertEquals(9, quantifier.stream().limit(9).count());
            assertEquals(expectedRule(), quantifier.getRule());
        }

        @Test
        @DisplayName("oneOrMore()")
        default void oneOrMore() throws Exception {
            var target = createTarget();
            var builder = target.oneOrMore();

            assertInstanceOf(QuantifierRule.Builder.class, builder);

            var quantifier = builder.build();

            assertInstanceOf(QuantifierRule.class, quantifier);
            assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
            assertEquals(1, quantifier.getMinCount());
            assertEquals(true, quantifier.getMaxCount().isEmpty());
            assertEquals(9, quantifier.stream().limit(9).count());
            assertEquals(expectedRule(), quantifier.getRule());
        }

    }

    @Override
    Quantifiable<T> createTarget();

    @Test
    @DisplayName("exactly(times:int) [times < 0]")
    default void exactlyWhenTimesIsNegative(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(IllegalArgumentException.class, () -> target.exactly(-1));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("atLeast(times:int) [times < 0]")
    default void atLeastWhenTimesIsNegative(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(IllegalArgumentException.class, () -> target.atLeast(-1));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("range(min:int, max:int) [min < 0]")
    default void rangeWhenMinIsNegative(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(IllegalArgumentException.class, () -> target.range(-1, 0));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @ParameterizedTest
    @CsvSource({
            "0,-1", "1,0", "2,1"
    })
    @DisplayName("range(min:int, max:int) [max < min]")
    default void rangeWhenMaxIsLessThanMin(int min, int max, TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(IllegalArgumentException.class, () -> target.range(min, max));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    @DisplayName("exactly(int)")
    default void exactly(int times) throws Exception {
        var target = createTarget();
        var quantifier = target.exactly(times);

        assertInstanceOf(QuantifierRule.class, quantifier);
        assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
        assertEquals(times, quantifier.getMinCount());
        assertEquals(times, quantifier.getMaxCount().getAsInt());
        assertEquals(times, quantifier.stream().limit(9).count());
        assertEquals(target, quantifier.getRule());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    @DisplayName("atLeast(int)")
    default void atLeast(int times) throws Exception {
        var target = createTarget();
        var quantifier = target.atLeast(times);

        assertInstanceOf(QuantifierRule.class, quantifier);
        assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
        assertEquals(times, quantifier.getMinCount());
        assertEquals(true, quantifier.getMaxCount().isEmpty());
        assertEquals(9, quantifier.stream().limit(9).count());
        assertEquals(target, quantifier.getRule());
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

        assertInstanceOf(QuantifierRule.class, quantifier);
        assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
        assertEquals(min, quantifier.getMinCount());
        assertEquals(max, quantifier.getMaxCount().getAsInt());
        assertEquals(max, quantifier.stream().limit(9).count());
        assertEquals(target, quantifier.getRule());
    }

    @Test
    @DisplayName("opt()")
    default void opt() throws Exception {
        var target = createTarget();
        var quantifier = target.opt();

        assertInstanceOf(QuantifierRule.class, quantifier);
        assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
        assertEquals(0, quantifier.getMinCount());
        assertEquals(1, quantifier.getMaxCount().getAsInt());
        assertEquals(1, quantifier.stream().limit(9).count());
        assertEquals(target, quantifier.getRule());
    }

    @Test
    @DisplayName("zeroOrMore()")
    default void zeroOrMore() throws Exception {
        var target = createTarget();
        var quantifier = target.zeroOrMore();

        assertInstanceOf(QuantifierRule.class, quantifier);
        assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
        assertEquals(0, quantifier.getMinCount());
        assertEquals(true, quantifier.getMaxCount().isEmpty());
        assertEquals(9, quantifier.stream().limit(9).count());
        assertEquals(target, quantifier.getRule());
    }

    @Test
    @DisplayName("oneOrMore()")
    default void oneOrMore() throws Exception {
        var target = createTarget();
        var quantifier = target.oneOrMore();

        assertInstanceOf(QuantifierRule.class, quantifier);
        assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
        assertEquals(1, quantifier.getMinCount());
        assertEquals(true, quantifier.getMaxCount().isEmpty());
        assertEquals(9, quantifier.stream().limit(9).count());
        assertEquals(target, quantifier.getRule());
    }

}
