package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

interface CompositeRuleTest<U extends Rule & Quantifiable> extends RuleTest, QuantifiableTest {

    @Nested
    interface BuilderTest<T extends Rule.Builder & Quantifiable> extends RuleTest.BulderTest, QuantifiableTest {

        T createEmptyBuilder();

        @Override
        T createTarget();

        @Test
        @DisplayName("build() [No elements]")
        default void buildInCaseOfNoElements() throws Exception {
            var builder = createEmptyBuilder();

            assertThrows(IllegalStateException.class, () -> builder.build());
        }

        @Test
        @DisplayName("exactly(int) [No elements]")
        default void exactlyInCaseOfNoElements() throws Exception {
            var builder = createEmptyBuilder();

            assertThrows(IllegalStateException.class, () -> builder.exactly(0));
        }

        @Test
        @DisplayName("atLeast(int) [No elements]")
        default void atLeastInCaseOfNoElements() throws Exception {
            var builder = createEmptyBuilder();

            assertThrows(IllegalStateException.class, () -> builder.atLeast(0));
        }

        @Test
        @DisplayName("range(int, int) [No elements]")
        default void rangeInCaseOfNoElements() throws Exception {
            var builder = createEmptyBuilder();

            assertThrows(IllegalStateException.class, () -> builder.range(0, 0));
        }

        @Test
        @DisplayName("opt() [No elements]")
        default void optInCaseOfNoElements() throws Exception {
            var builder = createEmptyBuilder();

            assertThrows(IllegalStateException.class, () -> builder.opt());
        }

        @Test
        @DisplayName("zeroOrMore() [No elements]")
        default void zeroOrMoreInCaseOfNoElements() throws Exception {
            var builder = createEmptyBuilder();

            assertThrows(IllegalStateException.class, () -> builder.zeroOrMore());
        }

        @Test
        @DisplayName("oneOrMore() [No elements]")
        default void oneOrMoreInCaseOfNoElements() throws Exception {
            var builder = createEmptyBuilder();

            assertThrows(IllegalStateException.class, () -> builder.oneOrMore());
        }

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
