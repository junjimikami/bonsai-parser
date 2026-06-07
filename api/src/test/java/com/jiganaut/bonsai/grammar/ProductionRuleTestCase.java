package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

/**
 *
 * @author Junji Mikami
 */
interface ProductionRuleTestCase<T> extends RuleTestCase<T> {

    @Override
    ProductionRule<T> createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.PRODUCTION;
    }

    String expectedSymbol();

    Rule<T> expectedRule();

    @Test
    @DisplayName("getSymbol()")
    default void getSymbol() throws Exception {
        var target = createTarget();

        assertEquals(expectedSymbol(), target.getSymbol());
    }

    @Test
    @DisplayName("getRule()")
    default void getRule() throws Exception {
        var target = createTarget();

        assertEquals(expectedRule(), target.getRule());
    }

}
