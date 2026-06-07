package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

/**
 *
 * @author Junji Mikami
 */
interface SkipRuleTestCase<T> extends RuleTestCase<T> {

    interface BuilderTestCase<T> extends RuleTestCase.BuilderTestCase<T> {

        @Override
        SkipRule.Builder<T> createTarget();

        @Override
        SkipRule<T> expectedRule();

        @Test
        @DisplayName("build()")
        default void build() throws Exception {
            var target = createTarget();
            var rule = target.build();

            assertEquals(expectedRule(), rule);
        }
    }

    @Override
    SkipRule<T> createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.SKIP;
    }

    Skippable<T> expectedRule();

    @Test
    @DisplayName("getRule()")
    default void getRule() throws Exception {
        var target = createTarget();

        assertEquals(expectedRule(), target.getRule());
    }

}
