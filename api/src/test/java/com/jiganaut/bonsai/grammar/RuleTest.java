package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.jiganaut.bonsai.grammar.Rule.Kind;

interface RuleTest {

    interface BulderTest {
        Rule.Builder builder();

        @Test
        @DisplayName("build(ps:ProductionSet) [Post-build operation]")
        default void buildPsInCasePostBuild() throws Exception {
            var builder = builder();
            builder.build(Stubs.DUMMY_PRODUCTION_SET);

            assertThrows(IllegalStateException.class, () -> builder.build(null));
        }

    }

    Rule build();

    Kind kind();

    RuleVisitor<Object[], String> visitor();

    @Test
    @DisplayName("objectMethods()")
    default void objectMethods() throws Exception {
        var rule = build();

        rule.toString();
    }

    @Test
    @DisplayName("getKind()")
    default void getKind() throws Exception {
        var rule = build();

        assertEquals(kind(), rule.getKind());
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor) [Null parameter]")
    default void acceptEvInCaseNullParameter() throws Exception {
        var rule = build();

        assertThrows(NullPointerException.class, () -> rule.accept(null));
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor)")
    default void acceptEv() throws Exception {
        var rule = build();
        var visitor = visitor();
        var expected = new Object[] { rule, null };
        var result = rule.accept(visitor);

        assertArrayEquals(expected, result);
    }

    @DisplayName("accept(ev:ElementVisitor, p:P)")
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { "test" })
    default void acceptEvP(String arg) throws Exception {
        var rule = build();
        var visitor = visitor();
        var expected = new Object[] { rule, arg };
        var result = rule.accept(visitor, arg);

        assertArrayEquals(expected, result);
    }
}
