package com.jiganaut.bonsai.grammar;

import org.junit.jupiter.api.Nested;

/**
 * 
 * @author Junji Mikami
 */
class SkipRuleTest {

    @Nested
    class TestCase1 implements SkipRuleTestCase {

        @Override
        public SkipRule createTarget() {
            return expectedRule().skip();
        }

        @Override
        public Skippable expectedRule() {
            return PatternRule.of("\\s");
        }

    }

}
