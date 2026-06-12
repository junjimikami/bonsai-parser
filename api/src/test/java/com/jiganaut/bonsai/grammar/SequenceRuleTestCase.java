package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.MockFactory.mockRule;
import static com.jiganaut.bonsai.grammar.MockFactory.mockRuleBuilder;
import static com.jiganaut.bonsai.grammar.MockFactory.mockSequenceRuleBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.grammar.Rule.Kind;

/**
 *
 * @author Junji Mikami
 */
interface SequenceRuleTestCase<T> extends CompositeRuleTestCase<T> {

    interface BuilderTestCase<T> extends CompositeRuleTestCase.BuilderTestCase<T> {

        @Override
        SequenceRule.Builder<T> createTarget();

        @Override
        SequenceRule<T> expectedRule();

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(rb:Rule.Builder) [rb == null]")
        default void addRbWhenRbIsNull(TestReporter testReporter) throws Exception {
            var target = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> target.add((Rule.Builder<T>) null));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("addAll(rb:SequenceRule.Builder) [rb == null]")
        default void addAllRbWhenRbIsNull(TestReporter testReporter) throws Exception {
            var target = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> target.addAll((null)));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(r:Rule) [Post-build]")
        default void addRWhenPostBuild(TestReporter testReporter) throws Exception {
            var target = createTarget();
            target.build();

            var ex = assertThrows(IllegalStateException.class, () -> target.add(mockRule()));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(rb:Rule.Builder) [Post-build]")
        default void addRbWhenPostBuild(TestReporter testReporter) throws Exception {
            var target = createTarget();
            target.build();

            var ex = assertThrows(IllegalStateException.class, () -> target.add(mockRuleBuilder()));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("addAll(rb:Rule.Builder) [Post-build]")
        default void addAllRbWhenPostBuild(TestReporter testReporter) throws Exception {
            var target = createTarget();
            target.build();

            var ex = assertThrows(IllegalStateException.class, () -> target.addAll(mockSequenceRuleBuilder()));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @Test
        @DisplayName("add(r:Rule)")
        default void addR() throws Exception {
            var target = createTarget();

            assertEquals(target, target.add(mockRule()));
        }

        @Test
        @DisplayName("add(rb:Rule.Builder)")
        default void addRb() throws Exception {
            var target = createTarget();

            assertEquals(target, target.add(mockRuleBuilder()));
        }

        @Test
        @DisplayName("addAll(rb:Rule.Builder)")
        default void addAllRb() throws Exception {
            var target = createTarget();

            assertEquals(target, target.addAll(mockSequenceRuleBuilder()));
        }

        @Test
        @DisplayName("build()")
        default void build() throws Exception {
            var target = createTarget();
            var rule = target.build();

            assertNotNull(rule);
            assertIterableEquals(expectedRule().getRules(), rule.getRules());
        }

        @Test
        @DisplayName("iterate()")
        default void iterate() throws Exception {
            var target = createTarget();

            var rules = new ArrayList<Rule<T>>();
            target.forEach(e -> rules.add(e.build()));
            assertIterableEquals(expectedRule().getRules(), rules);
        }

    }

    @Override
    SequenceRule<T> createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.SEQUENCE;
    }

    List<Rule<T>> expectedRules();

    @Test
    @DisplayName("getRules()")
    default void getRules() throws Exception {
        var target = createTarget();

        assertIterableEquals(expectedRules(), target.getRules());
    }

}
