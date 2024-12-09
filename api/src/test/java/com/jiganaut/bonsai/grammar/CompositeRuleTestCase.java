package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

/**
 * 
 * @author Junji Mikami
 */
interface CompositeRuleTestCase<U extends Rule & Quantifiable> extends RuleTestCase, QuantifiableTestCase {

    @Nested
    interface BuilderTestCase<T extends Rule.Builder & Quantifiable> extends RuleTestCase.BuilderTestCase, QuantifiableTestCase {

        @Override
        T createTarget();

        @SuppressWarnings("exports")
        @Test
        @DisplayName("exactly(int) [Post-build operation]")
        default void exactlyInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.exactly(0));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("atLeast(int) [Post-build operation]")
        default void atLeastInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.atLeast(0));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("range(int, int) [Post-build operation]")
        default void rangeInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.range(0, 0));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("opt() [Post-build operation]")
        default void optInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.opt());
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("zeroOrMore() [Post-build operation]")
        default void zeroOrMoreInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.zeroOrMore());
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("oneOrMore() [Post-build operation]")
        default void oneOrMoreInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.oneOrMore());
            testReporter.publishEntry(ex.getMessage());
        }

    }

    @Override
    U createTarget();

}
