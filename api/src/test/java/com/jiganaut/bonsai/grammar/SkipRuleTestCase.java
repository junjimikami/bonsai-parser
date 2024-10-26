package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

interface SkipRuleTestCase extends RuleTestCase {

    @Override
    SkipRule createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.SKIP;
    }

    Skippable expectedRule();

    @Test
    @DisplayName("getRule()")
    default void getRule() throws Exception {
        var target = createTarget();

        assertEquals(expectedRule(), target.getRule());
    }

}
