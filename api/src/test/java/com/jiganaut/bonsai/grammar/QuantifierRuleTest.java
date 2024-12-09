package com.jiganaut.bonsai.grammar;

import java.util.OptionalInt;

import org.junit.jupiter.api.Nested;

/**
 * 
 * @author Junji Mikami
 */
class QuantifierRuleTest {

    @Nested
    class TestCase1 implements QuantifierRuleTestCase {

        @Override
        public QuantifierRule createTarget() {
            return ChoiceRule.builder().opt();
        }

        @Override
        public int expectedMinCount() {
            return 0;
        }

        @Override
        public OptionalInt expectedMaxCount() {
            return OptionalInt.of(1);
        }

        @Override
        public Rule expectedRule() {
            return ChoiceRule.of();
        }

    }

    @Nested
    class TestCase2 implements QuantifierRuleTestCase {

        @Override
        public QuantifierRule createTarget() {
            return SequenceRule.builder().opt();
        }

        @Override
        public int expectedMinCount() {
            return 0;
        }

        @Override
        public OptionalInt expectedMaxCount() {
            return OptionalInt.of(1);
        }

        @Override
        public Rule expectedRule() {
            return SequenceRule.of();
        }

    }

}
