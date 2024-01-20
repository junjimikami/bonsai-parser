package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

interface ExpressionTest {

    @FunctionalInterface
    interface BulderTest {
        Expression.Builder builder();

        @Test
        @DisplayName("build() [Post-build operation]")
        default void buildInCasePostBuild() throws Exception {
            var builder = builder();
            builder.build(Stubs.DUMMY_PRODUCTION_SET);

            assertThrows(IllegalStateException.class, () -> builder.build())
                    .printStackTrace();
        }

        @Test
        @DisplayName("build(ps:ProductionSet) [Post-build operation]")
        default void buildPsInCasePostBuild() throws Exception {
            var builder = builder();
            builder.build(Stubs.DUMMY_PRODUCTION_SET);

            assertThrows(IllegalStateException.class, () -> builder.build(null))
                    .printStackTrace();
        }

    }

    Expression build();

    @Test
    @DisplayName("accept(ev:ElementVisitor) [Null parameter]")
    default void acceptEvInCaseNullParameter() throws Exception {
        var expression = build();

        assertThrows(NullPointerException.class, () -> expression.accept(null));
    }

}
