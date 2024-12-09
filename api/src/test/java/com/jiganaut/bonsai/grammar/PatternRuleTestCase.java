package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

/**
 * 
 * @author Junji Mikami
 */
interface PatternRuleTestCase extends RuleTestCase, QuantifiableTestCase, SkippableTestCase {

    @Override
    PatternRule createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.PATTERN;
    }

    String expectedPattern();

    @Test
    @DisplayName("getPattern()")
    default void getPattern() throws Exception {
        var target = createTarget();

        assertEquals(expectedPattern(), target.getPattern().pattern());
    }
}
