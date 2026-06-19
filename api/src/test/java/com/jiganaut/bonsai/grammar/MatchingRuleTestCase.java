package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.MockFactory.mockToken;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

/**
 *
 * @author Junji Mikami
 */
interface MatchingRuleTestCase<T> extends QuantifiableTestCase<T>, SkippableTestCase<T> {

    @Override
    MatchingRule<T> createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.MATCH;
    }

    String expectedName();

    T expectedValue();

    @Test
    @DisplayName("test(t:Token)")
    default void testT() throws Exception {
        var target = createTarget();
        var token = mockToken(expectedName(), expectedValue());

        assertTrue(target.test(token));
    }

    @Test
    @DisplayName("test(name:String, value:T)")
    default void testNameValue() throws Exception {
        var target = createTarget();

        assertTrue(target.test(expectedName(), expectedValue()));
    }

}
