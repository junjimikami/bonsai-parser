package com.jiganaut.bonsai.grammar;

import org.junit.jupiter.api.Nested;

/**
 *
 * @author Junji Mikami
 */
class MatchingRuleTest {

    @Nested
    class TestCase1 implements MatchingRuleTestCase<String> {

        @Override
        public MatchingRule<String> createTarget() {
            return Rules.token(expectedName(), expectedValue());
        }

        @Override
        public String expectedName() {
            return "NAME";
        }

        @Override
        public String expectedValue() {
            return "VALUE";
        }

    }

}
