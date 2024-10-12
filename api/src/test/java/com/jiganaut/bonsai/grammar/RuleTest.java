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

interface RuleTest extends TestCase {

    interface BuilderTest extends TestCase {
        Rule.Builder createTarget();
 
        default Rule.Builder createTargetBuilder() {
            return createTarget();
        }

        @Test
        @DisplayName("build() [Post-build operation]")
        default void buildInCaseOfPostBuild() throws Exception {
            var builder = createTargetBuilder();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.build());
        }

    }

    Rule createTarget();

    default Rule createTargetRule() {
        return createTarget();
    }

    Kind expectedKind();

    RuleVisitor<Object[], String> createVisitor();

    @Test
    @DisplayName("objectMethods()")
    default void objectMethods() throws Exception {
        var rule = createTargetRule();

        rule.toString();
    }

    @Test
    @DisplayName("getKind()")
    default void getKind() throws Exception {
        var rule = createTargetRule();

        assertEquals(expectedKind(), rule.getKind());
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor) [Null parameter]")
    default void acceptEvInCaseOfNullParameter() throws Exception {
        var rule = createTargetRule();

        assertThrows(NullPointerException.class, () -> rule.accept(null));
    }

    @Test
    @DisplayName("accept(rv:RuleVisitor)")
    default void acceptRv() throws Exception {
        var rule = createTargetRule();
        var visitor = createVisitor();
        var expected = new Object[] { rule, null };
        var result = rule.accept(visitor);
        var result2 = visitor.visit(rule);

        assertArrayEquals(expected, result);
        assertArrayEquals(expected, result2);
    }

    @DisplayName("accept(rv:RuleVisitor, p:P)")
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { "test" })
    default void acceptRvP(String arg) throws Exception {
        var rule = createTargetRule();
        var visitor = createVisitor();
        var expected = new Object[] { rule, arg };
        var result = rule.accept(visitor, arg);
        var result2 = visitor.visit(rule, arg);

        assertArrayEquals(expected, result);
        assertArrayEquals(expected, result2);
    }
}
