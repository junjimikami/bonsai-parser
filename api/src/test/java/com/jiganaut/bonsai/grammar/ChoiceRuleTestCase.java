package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

/**
 * 
 * @author Junji Mikami
 *
 */
interface ChoiceRuleTestCase extends CompositeRuleTestCase<ChoiceRule> {

    @Nested
    interface BuilderTestCase extends CompositeRuleTestCase.BuilderTestCase<ChoiceRule.Builder> {

        @Override
        ChoiceRule.Builder createTarget();

        @Override
        ChoiceRule expectedRule();

        @Test
        @DisplayName("add(r:Rule) [Null parameter]")
        default void addRInCaseOfNullParameter() throws Exception {
            var builder = createTarget();

            assertThrows(NullPointerException.class, () -> builder.add((Rule) null));
        }

        @Test
        @DisplayName("add(rb:Rule.Builder) [Null parameter]")
        default void addRbInCaseOfNullParameter() throws Exception {
            var builder = createTarget();

            assertThrows(NullPointerException.class, () -> builder.add((Rule.Builder) null));
        }

        @Test
        @DisplayName("add(r:Rule) [Post-build operation]")
        default void addRInCaseOfPostBuild() throws Exception {
            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.add(mock(Rule.class)));
        }

        @Test
        @DisplayName("add(rb:Rule.Builder) [Post-build operation]")
        default void addRbInCaseOfPostBuild() throws Exception {
            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.add(mock(Rule.Builder.class)));
        }

        @Test
        @DisplayName("add(r:Rule)")
        default void addR() throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.add(mock(Rule.class)));
        }

        @Test
        @DisplayName("add(rb:Rule.Builder)")
        default void addRb() throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.add(mock(Rule.Builder.class)));
        }

        @Test
        @DisplayName("addEmpty()")
        default void addEmpty() throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.addEmpty());
        }

        @Test
        @DisplayName("build()")
        @Override
        default void build() throws Exception {
            var builder = createTarget();
            var rule = builder.build();

            assertNotNull(rule);
            assertIterableEquals(expectedRule().getChoices(), rule.getChoices());
        }
        

        @Test
        @DisplayName("shortCircuit() [Post-build operation]")
        default void shortCircuitInCaseOfPostBuild() throws Exception {
            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.shortCircuit());
        }

        @Test
        @DisplayName("shortCircuit()")
        default void shortCircuit() throws Exception {
            var target = createTarget().shortCircuit();

            assertTrue(target.isShortCircuit());
        }

    }

    @Override
    ChoiceRule createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.CHOICE;
    }

    Set<? extends Rule> expectedChoices();

    default boolean expectedToBeShortCircuit() {
        return false;
    }

    @Test
    @DisplayName("getChoices()")
    default void getChoices() throws Exception {
        var target = createTarget();

        assertEquals(expectedChoices(), target.getChoices());
    }

    @Test
    @DisplayName("isShortCircuit()")
    default void isShortCircuit() throws Exception {
        var target = createTarget();

        assertEquals(expectedToBeShortCircuit(), target.isShortCircuit());
    }

    @Test
    @DisplayName("shortCircuit()")
    default void shortCircuit() throws Exception {
        var target = createTarget();

        assertNotSame(target, target.shortCircuit());
        assertTrue(target.shortCircuit().isShortCircuit());
    }

}
