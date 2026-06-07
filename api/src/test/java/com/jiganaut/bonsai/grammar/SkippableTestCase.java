package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Junji Mikami
 */
interface SkippableTestCase<T> extends RuleTestCase<T> {

    @Nested
    interface BuilderTestCase<T> extends RuleTestCase.BuilderTestCase<T> {

        @Override
        Skippable.Builder<T> createTarget();

        @Override
        Skippable<T> expectedRule();

        @Test
        @DisplayName("skip()")
        default void skip() throws Exception {
            var target = createTarget();
            var builder = target.skip();

            assertInstanceOf(SkipRule.Builder.class, builder);

            var skip = builder.build();

            assertInstanceOf(SkipRule.class, skip);
            assertEquals(Rule.Kind.SKIP, skip.getKind());
            assertEquals(expectedRule(), skip.getRule());
        }

    }

    @Override
    Skippable<T> createTarget();

    @Test
    @DisplayName("skip()")
    default void skip() throws Exception {
        var target = createTarget();
        var skip = target.skip();

        assertInstanceOf(SkipRule.class, skip);
        assertEquals(Rule.Kind.SKIP, skip.getKind());
        assertEquals(target, skip.getRule());
    }

}
