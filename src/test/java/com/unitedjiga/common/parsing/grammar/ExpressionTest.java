package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.unitedjiga.common.parsing.grammar.Expression.Kind;

interface ExpressionTest {

    interface BulderTest {
        Expression.Builder builder();

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

    Kind kind();

    ExpressionVisitor<Object[], String> elementVisitor();

    @Test
    @DisplayName("objectMethods()")
    default void objectMethods() throws Exception {
        var expression = build();

        expression.toString();
    }

    @Test
    @DisplayName("getKind()")
    default void getKind() throws Exception {
        var expression = build();

        assertEquals(kind(), expression.getKind());
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor) [Null parameter]")
    default void acceptEvInCaseNullParameter() throws Exception {
        var expression = build();

        assertThrows(NullPointerException.class, () -> expression.accept(null));
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor)")
    default void acceptEv() throws Exception {
        var expression = build();
        var visitor = elementVisitor();
        var expected = new Object[] { expression, null };
        var result = expression.accept(visitor);

        assertArrayEquals(expected, result);
    }

    @DisplayName("accept(ev:ElementVisitor, p:P)")
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { "test" })
    default void acceptEvP(String arg) throws Exception {
        var expression = build();
        var visitor = elementVisitor();
        var expected = new Object[] { expression, arg };
        var result = expression.accept(visitor, arg);

        assertArrayEquals(expected, result);
    }
}
