package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

interface CompositeRuleTestCase<U extends Rule & Quantifiable> extends RuleTestCase, QuantifiableTestCase {

    @Nested
    interface BuilderTestCase<T extends Rule.Builder & Quantifiable> extends RuleTestCase.BuilderTestCase, QuantifiableTestCase {

        @Override
        T createTarget();

        @Test
        @DisplayName("exactly(int) [Post-build operation]")
        default void exactlyInCaseOfPostBuild() throws Exception {
            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.exactly(0));
        }

        @Test
        @DisplayName("atLeast(int) [Post-build operation]")
        default void atLeastInCaseOfPostBuild() throws Exception {
            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.atLeast(0));
        }

        @Test
        @DisplayName("range(int, int) [Post-build operation]")
        default void rangeInCaseOfPostBuild() throws Exception {
            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.range(0, 0));
        }

        @Test
        @DisplayName("opt() [Post-build operation]")
        default void optInCaseOfPostBuild() throws Exception {
            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.opt());
        }

        @Test
        @DisplayName("zeroOrMore() [Post-build operation]")
        default void zeroOrMoreInCaseOfPostBuild() throws Exception {
            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.zeroOrMore());
        }

        @Test
        @DisplayName("oneOrMore() [Post-build operation]")
        default void oneOrMoreInCaseOfPostBuild() throws Exception {
            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.oneOrMore());
        }

    }

    @Override
    U createTarget();

}
