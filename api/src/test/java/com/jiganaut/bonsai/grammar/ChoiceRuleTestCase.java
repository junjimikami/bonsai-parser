package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.GrammarMockFactory.mockChoiceRuleBuilder;
import static com.jiganaut.bonsai.grammar.GrammarMockFactory.mockRule;
import static com.jiganaut.bonsai.grammar.GrammarMockFactory.mockRuleBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.grammar.Rule.Kind;

/**
 *
 * @author Junji Mikami
 *
 */
interface ChoiceRuleTestCase<T> extends CompositeRuleTestCase<T>, SkippableTestCase<T> {

    @Nested
    interface BuilderTestCase<T> extends CompositeRuleTestCase.BuilderTestCase<T>, SkippableTestCase.BuilderTestCase<T> {

        @Override
        ChoiceRule.Builder<T> createTarget();

        @Override
        ChoiceRule<T> expectedRule();

        default boolean expectedShortCircuit() {
            return false;
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(rb:Rule.Builder) [rb == null]")
        default void addRbWhenRbIsNull(TestReporter testReporter) throws Exception {
            var builder = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> builder.add((Rule.Builder<T>) null));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("addAll(rb:ChoiceRule.Builder) [rb == null]")
        default void addAllRbWhenRbIsNull(TestReporter testReporter) throws Exception {
            var builder = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> builder.addAll((ChoiceRule.Builder<T>) null));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(r:Rule) [Post-build]")
        default void addRWhenPostBuild(TestReporter testReporter) throws Exception {
            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.add(mockRule()));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(rb:Rule.Builder) [Post-build]")
        default void addRbWhenPostBuild(TestReporter testReporter) throws Exception {
            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.add(mockRuleBuilder()));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("addAll(rb:ChoiceRule.Builder) [Post-build]")
        default void addAllRbWhenPostBuild(TestReporter testReporter) throws Exception {
            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.addAll(mockChoiceRuleBuilder()));
            testReporter.publishEntry(ex.getMessage());
        }

        @Test
        @DisplayName("add(r:Rule)")
        default void addR() throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.add(mockRule()));
        }

        @Test
        @DisplayName("add(rb:Rule.Builder)")
        default void addRb() throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.add(mockRuleBuilder()));
        }

        @Test
        @DisplayName("addAll(rb:ChoiceRule.Builder)")
        default void addAll() throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.addAll(mockChoiceRuleBuilder()));
        }

        @Test
        @DisplayName("addEmpty()")
        default void addEmpty() throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.addEmpty());
        }

        @Test
        @DisplayName("asShortCircuit()")
        default void asShortCircuit() throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.asShortCircuit());
        }

        @Test
        @DisplayName("build()")
        @Override
        default void build() throws Exception {
            var builder = createTarget();
            var rule = builder.build();

            assertNotNull(rule);
            assertIterableEquals(expectedRule().getChoices(), rule.getChoices());
            assertEquals(expectedShortCircuit(), rule.isShortCircuit());
        }

        @Test
        @DisplayName("iterate()")
        default void iterate() throws Exception {
            var target = createTarget();

            var rules = new ArrayList<Rule<T>>();
            target.forEach(e -> rules.add(e.build()));
            assertIterableEquals(expectedRule().getChoices(), rules);
        }

    }

    @Override
    ChoiceRule<T> createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.CHOICE;
    }

    Set<? extends Rule<T>> expectedChoices();

    default boolean expectedShortCircuit() {
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

        assertEquals(expectedShortCircuit(), target.isShortCircuit());
    }

}
