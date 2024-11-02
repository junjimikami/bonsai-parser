package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

interface SequenceRuleTestCase extends CompositeRuleTestCase<SequenceRule> {

    interface BuilderTestCase extends CompositeRuleTestCase.BuilderTestCase<SequenceRule.Builder> {

        @Override
        SequenceRule.Builder createTarget();

        @Override
        SequenceRule expectedRule();

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
        @DisplayName("build()")
        default void build() throws Exception {
            var builder = createTarget();
            var rule = builder.build();

            assertNotNull(rule);
            assertIterableEquals(expectedRule().getRules(), rule.getRules());
        }

    }

    @Override
    SequenceRule createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.SEQUENCE;
    }

    List<? extends Rule> expectedRules();

    @Test
    @DisplayName("getRules()")
    default void getRules() throws Exception {
        var target = createTarget();

        assertIterableEquals(expectedRules(), target.getRules());
    }

}
