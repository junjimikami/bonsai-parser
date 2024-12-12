package com.jiganaut.bonsai.grammar;

import java.util.OptionalInt;

import org.junit.jupiter.api.Nested;

/**
 * 
 * @author Junji Mikami
 */
class QuantifierRuleTest {

    @Nested
    class ChoiceRuleTestCase {

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
                return ChoiceRule.builder().zeroOrMore();
            }

            @Override
            public int expectedMinCount() {
                return 0;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.empty();
            }

            @Override
            public Rule expectedRule() {
                return ChoiceRule.of();
            }

        }

        @Nested
        class TestCase3 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return ChoiceRule.builder().oneOrMore();
            }

            @Override
            public int expectedMinCount() {
                return 1;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.empty();
            }

            @Override
            public Rule expectedRule() {
                return ChoiceRule.of();
            }

        }

        @Nested
        class TestCase4 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return ChoiceRule.builder().atLeast(2);
            }

            @Override
            public int expectedMinCount() {
                return 2;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.empty();
            }

            @Override
            public Rule expectedRule() {
                return ChoiceRule.of();
            }

        }

        @Nested
        class TestCase5 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return ChoiceRule.builder().exactly(2);
            }

            @Override
            public int expectedMinCount() {
                return 2;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.of(2);
            }

            @Override
            public Rule expectedRule() {
                return ChoiceRule.of();
            }

        }

        @Nested
        class TestCase6 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return ChoiceRule.builder().range(1, 2);
            }

            @Override
            public int expectedMinCount() {
                return 1;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.of(2);
            }

            @Override
            public Rule expectedRule() {
                return ChoiceRule.of();
            }

        }

    }

    @Nested
    class SequenceRuleTestCase {

        @Nested
        class TestCase1 implements QuantifierRuleTestCase {

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

        @Nested
        class TestCase2 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return SequenceRule.builder().zeroOrMore();
            }

            @Override
            public int expectedMinCount() {
                return 0;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.empty();
            }

            @Override
            public Rule expectedRule() {
                return SequenceRule.of();
            }

        }

        @Nested
        class TestCase3 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return SequenceRule.builder().oneOrMore();
            }

            @Override
            public int expectedMinCount() {
                return 1;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.empty();
            }

            @Override
            public Rule expectedRule() {
                return SequenceRule.of();
            }

        }

        @Nested
        class TestCase4 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return SequenceRule.builder().atLeast(2);
            }

            @Override
            public int expectedMinCount() {
                return 2;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.empty();
            }

            @Override
            public Rule expectedRule() {
                return SequenceRule.of();
            }

        }

        @Nested
        class TestCase5 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return SequenceRule.builder().exactly(2);
            }

            @Override
            public int expectedMinCount() {
                return 2;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.of(2);
            }

            @Override
            public Rule expectedRule() {
                return SequenceRule.of();
            }

        }

        @Nested
        class TestCase6 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return SequenceRule.builder().range(1, 2);
            }

            @Override
            public int expectedMinCount() {
                return 1;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.of(2);
            }

            @Override
            public Rule expectedRule() {
                return SequenceRule.of();
            }

        }

    }

    @Nested
    class PatternRuleTestCase {

        @Nested
        class TestCase1 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return PatternRule.of("").opt();
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
                return PatternRule.of("");
            }

        }

        @Nested
        class TestCase2 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return PatternRule.of("").zeroOrMore();
            }

            @Override
            public int expectedMinCount() {
                return 0;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.empty();
            }

            @Override
            public Rule expectedRule() {
                return PatternRule.of("");
            }

        }

        @Nested
        class TestCase3 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return PatternRule.of("").oneOrMore();
            }

            @Override
            public int expectedMinCount() {
                return 1;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.empty();
            }

            @Override
            public Rule expectedRule() {
                return PatternRule.of("");
            }

        }

        @Nested
        class TestCase4 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return PatternRule.of("").atLeast(2);
            }

            @Override
            public int expectedMinCount() {
                return 2;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.empty();
            }

            @Override
            public Rule expectedRule() {
                return PatternRule.of("");
            }

        }

        @Nested
        class TestCase5 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return PatternRule.of("").exactly(2);
            }

            @Override
            public int expectedMinCount() {
                return 2;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.of(2);
            }

            @Override
            public Rule expectedRule() {
                return PatternRule.of("");
            }

        }

        @Nested
        class TestCase6 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return PatternRule.of("").range(1, 2);
            }

            @Override
            public int expectedMinCount() {
                return 1;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.of(2);
            }

            @Override
            public Rule expectedRule() {
                return PatternRule.of("");
            }

        }

    }

    @Nested
    class ReferenceRuleTestCase {

        @Nested
        class TestCase1 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return ReferenceRule.of("").opt();
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
                return ReferenceRule.of("");
            }

        }

        @Nested
        class TestCase2 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return ReferenceRule.of("").zeroOrMore();
            }

            @Override
            public int expectedMinCount() {
                return 0;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.empty();
            }

            @Override
            public Rule expectedRule() {
                return ReferenceRule.of("");
            }

        }

        @Nested
        class TestCase3 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return ReferenceRule.of("").oneOrMore();
            }

            @Override
            public int expectedMinCount() {
                return 1;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.empty();
            }

            @Override
            public Rule expectedRule() {
                return ReferenceRule.of("");
            }

        }

        @Nested
        class TestCase4 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return ReferenceRule.of("").atLeast(2);
            }

            @Override
            public int expectedMinCount() {
                return 2;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.empty();
            }

            @Override
            public Rule expectedRule() {
                return ReferenceRule.of("");
            }

        }

        @Nested
        class TestCase5 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return ReferenceRule.of("").exactly(2);
            }

            @Override
            public int expectedMinCount() {
                return 2;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.of(2);
            }

            @Override
            public Rule expectedRule() {
                return ReferenceRule.of("");
            }

        }

        @Nested
        class TestCase6 implements QuantifierRuleTestCase {

            @Override
            public QuantifierRule createTarget() {
                return ReferenceRule.of("").range(1, 2);
            }

            @Override
            public int expectedMinCount() {
                return 1;
            }

            @Override
            public OptionalInt expectedMaxCount() {
                return OptionalInt.of(2);
            }

            @Override
            public Rule expectedRule() {
                return ReferenceRule.of("");
            }

        }

    }

}
