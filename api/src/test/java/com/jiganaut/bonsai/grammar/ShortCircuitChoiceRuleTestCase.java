package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

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
interface ShortCircuitChoiceRuleTestCase extends ChoiceRuleTestCase {

    @Nested
    interface BuilderTestCase extends ChoiceRuleTestCase.BuilderTestCase {

        @Override
        ShortCircuitChoiceRule.Builder createTarget();

        @Override
        ShortCircuitChoiceRule expectedRule();

    }

    @Override
    ShortCircuitChoiceRule createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.SHORT_CIRCUIT_CHOICE;
    }

    Set<? extends Rule> expectedChoices();

    @Override
    @Test
    @DisplayName("getChoices()")
    default void getChoices() throws Exception {
        var target = createTarget();

        assertIterableEquals(expectedChoices(), target.getChoices());
    }

}
