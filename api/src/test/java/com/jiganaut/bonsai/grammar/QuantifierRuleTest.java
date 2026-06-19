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
        class TestCase1 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return ChoiceRule.<String>builder()
                        .build()
                        .opt();
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
            public ChoiceRule<String> expectedRule() {
                return ChoiceRule.<String>builder().build();
            }

        }

        @Nested
        class TestCase2 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return ChoiceRule.<String>builder()
                        .build()
                        .zeroOrMore();
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
            public ChoiceRule<String> expectedRule() {
                return ChoiceRule.<String>builder().build();
            }

        }

        @Nested
        class TestCase3 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return ChoiceRule.<String>builder()
                        .build()
                        .oneOrMore();
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
            public ChoiceRule<String> expectedRule() {
                return ChoiceRule.<String>builder().build();
            }

        }

        @Nested
        class TestCase4 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return ChoiceRule.<String>builder()
                        .build()
                        .atLeast(2);
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
            public ChoiceRule<String> expectedRule() {
                return ChoiceRule.<String>builder().build();
            }

        }

        @Nested
        class TestCase5 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return ChoiceRule.<String>builder()
                        .build()
                        .exactly(2);
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
            public ChoiceRule<String> expectedRule() {
                return ChoiceRule.<String>builder().build();
            }

        }

        @Nested
        class TestCase6 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return ChoiceRule.<String>builder()
                        .build()
                        .range(1, 2);
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
            public ChoiceRule<String> expectedRule() {
                return ChoiceRule.<String>builder().build();
            }

        }

        @Nested
        class BuilderTestCase1 implements QuantifierRuleTestCase.BuilderTestCase<String> {

            @Override
            public QuantifierRule.Builder<String> createTarget() {
                return ChoiceRule.<String>builder()
                        .opt();
            }

            @Override
            public QuantifierRule<String> expectedRule() {
                return ChoiceRule.<String>builder()
                        .opt()
                        .build();
            }

        }

        @Nested
        class BuilderTestCase2 implements QuantifierRuleTestCase.BuilderTestCase<String> {

            @Override
            public QuantifierRule.Builder<String> createTarget() {
                return ChoiceRule.<String>builder()
                        .zeroOrMore();
            }

            @Override
            public QuantifierRule<String> expectedRule() {
                return ChoiceRule.<String>builder()
                        .zeroOrMore()
                        .build();
            }

        }

        @Nested
        class BuilderTestCase3 implements QuantifierRuleTestCase.BuilderTestCase<String> {

            @Override
            public QuantifierRule.Builder<String> createTarget() {
                return ChoiceRule.<String>builder()
                        .oneOrMore();
            }

            @Override
            public QuantifierRule<String> expectedRule() {
                return ChoiceRule.<String>builder()
                        .oneOrMore()
                        .build();
            }

        }

        @Nested
        class BuilderTestCase4 implements QuantifierRuleTestCase.BuilderTestCase<String> {

            @Override
            public QuantifierRule.Builder<String> createTarget() {
                return ChoiceRule.<String>builder()
                        .atLeast(2);
            }

            @Override
            public QuantifierRule<String> expectedRule() {
                return ChoiceRule.<String>builder()
                        .atLeast(2)
                        .build();
            }

        }

        @Nested
        class BuilderTestCase5 implements QuantifierRuleTestCase.BuilderTestCase<String> {

            @Override
            public QuantifierRule.Builder<String> createTarget() {
                return ChoiceRule.<String>builder()
                        .exactly(2);
            }

            @Override
            public QuantifierRule<String> expectedRule() {
                return ChoiceRule.<String>builder()
                        .exactly(2)
                        .build();
            }

        }

        @Nested
        class BuilderTestCase6 implements QuantifierRuleTestCase.BuilderTestCase<String> {

            @Override
            public QuantifierRule.Builder<String> createTarget() {
                return ChoiceRule.<String>builder()
                        .range(1, 2);
            }

            @Override
            public QuantifierRule<String> expectedRule() {
                return ChoiceRule.<String>builder()
                        .range(1, 2)
                        .build();
            }

        }

    }

    @Nested
    class SequenceRuleTestCase {

        @Nested
        class TestCase1 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return SequenceRule.<String>builder()
                        .build()
                        .opt();
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
            public SequenceRule<String> expectedRule() {
                return SequenceRule.<String>builder().build();
            }

        }

        @Nested
        class TestCase2 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return SequenceRule.<String>builder()
                        .build()
                        .zeroOrMore();
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
            public SequenceRule<String> expectedRule() {
                return SequenceRule.<String>builder().build();
            }

        }

        @Nested
        class TestCase3 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return SequenceRule.<String>builder()
                        .build()
                        .oneOrMore();
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
            public SequenceRule<String> expectedRule() {
                return SequenceRule.<String>builder().build();
            }

        }

        @Nested
        class TestCase4 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return SequenceRule.<String>builder()
                        .build()
                        .atLeast(2);
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
            public SequenceRule<String> expectedRule() {
                return SequenceRule.<String>builder().build();
            }

        }

        @Nested
        class TestCase5 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return SequenceRule.<String>builder()
                        .build()
                        .exactly(2);
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
            public SequenceRule<String> expectedRule() {
                return SequenceRule.<String>builder().build();
            }

        }

        @Nested
        class TestCase6 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return SequenceRule.<String>builder()
                        .build()
                        .range(1, 2);
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
            public SequenceRule<String> expectedRule() {
                return SequenceRule.<String>builder().build();
            }

        }

        @Nested
        class BuilderTestCase1 implements QuantifierRuleTestCase.BuilderTestCase<String> {

            @Override
            public QuantifierRule.Builder<String> createTarget() {
                return SequenceRule.<String>builder()
                        .opt();
            }

            @Override
            public QuantifierRule<String> expectedRule() {
                return SequenceRule.<String>builder()
                        .opt()
                        .build();
            }

        }

        @Nested
        class BuilderTestCase2 implements QuantifierRuleTestCase.BuilderTestCase<String> {

            @Override
            public QuantifierRule.Builder<String> createTarget() {
                return SequenceRule.<String>builder()
                        .zeroOrMore();
            }

            @Override
            public QuantifierRule<String> expectedRule() {
                return SequenceRule.<String>builder()
                        .zeroOrMore()
                        .build();
            }

        }

        @Nested
        class BuilderTestCase3 implements QuantifierRuleTestCase.BuilderTestCase<String> {

            @Override
            public QuantifierRule.Builder<String> createTarget() {
                return SequenceRule.<String>builder()
                        .oneOrMore();
            }

            @Override
            public QuantifierRule<String> expectedRule() {
                return SequenceRule.<String>builder()
                        .oneOrMore()
                        .build();
            }

        }

        @Nested
        class BuilderTestCase4 implements QuantifierRuleTestCase.BuilderTestCase<String> {

            @Override
            public QuantifierRule.Builder<String> createTarget() {
                return SequenceRule.<String>builder()
                        .atLeast(2);
            }

            @Override
            public QuantifierRule<String> expectedRule() {
                return SequenceRule.<String>builder()
                        .atLeast(2)
                        .build();
            }

        }

        @Nested
        class BuilderTestCase5 implements QuantifierRuleTestCase.BuilderTestCase<String> {

            @Override
            public QuantifierRule.Builder<String> createTarget() {
                return SequenceRule.<String>builder()
                        .exactly(2);
            }

            @Override
            public QuantifierRule<String> expectedRule() {
                return SequenceRule.<String>builder()
                        .exactly(2)
                        .build();
            }

        }

        @Nested
        class BuilderTestCase6 implements QuantifierRuleTestCase.BuilderTestCase<String> {

            @Override
            public QuantifierRule.Builder<String> createTarget() {
                return SequenceRule.<String>builder()
                        .range(1, 2);
            }

            @Override
            public QuantifierRule<String> expectedRule() {
                return SequenceRule.<String>builder()
                        .range(1, 2)
                        .build();
            }

        }

    }

    @Nested
    class MatchingRuleTestCase {

        @Nested
        class TestCase1 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return Rules.pattern("").opt();
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
            public MatchingRule<String> expectedRule() {
                return Rules.pattern("");
            }

        }

        @Nested
        class TestCase2 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return Rules.pattern("").zeroOrMore();
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
            public MatchingRule<String> expectedRule() {
                return Rules.pattern("");
            }

        }

        @Nested
        class TestCase3 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return Rules.pattern("").oneOrMore();
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
            public MatchingRule<String> expectedRule() {
                return Rules.pattern("");
            }

        }

        @Nested
        class TestCase4 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return Rules.pattern("").atLeast(2);
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
            public MatchingRule<String> expectedRule() {
                return Rules.pattern("");
            }

        }

        @Nested
        class TestCase5 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return Rules.pattern("").exactly(2);
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
            public MatchingRule<String> expectedRule() {
                return Rules.pattern("");
            }

        }

        @Nested
        class TestCase6 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return Rules.pattern("").range(1, 2);
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
            public MatchingRule<String> expectedRule() {
                return Rules.pattern("");
            }

        }

    }

    @Nested
    class ReferenceRuleTestCase {

        @Nested
        class TestCase1 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return ReferenceRule.<String>of("").opt();
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
            public ReferenceRule<String> expectedRule() {
                return ReferenceRule.<String>of("");
            }

        }

        @Nested
        class TestCase2 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return ReferenceRule.<String>of("").zeroOrMore();
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
            public ReferenceRule<String> expectedRule() {
                return ReferenceRule.<String>of("");
            }

        }

        @Nested
        class TestCase3 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return ReferenceRule.<String>of("").oneOrMore();
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
            public ReferenceRule<String> expectedRule() {
                return ReferenceRule.<String>of("");
            }

        }

        @Nested
        class TestCase4 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return ReferenceRule.<String>of("").atLeast(2);
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
            public ReferenceRule<String> expectedRule() {
                return ReferenceRule.<String>of("");
            }

        }

        @Nested
        class TestCase5 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return ReferenceRule.<String>of("").exactly(2);
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
            public ReferenceRule<String> expectedRule() {
                return ReferenceRule.<String>of("");
            }

        }

        @Nested
        class TestCase6 implements QuantifierRuleTestCase<String> {

            @Override
            public QuantifierRule<String> createTarget() {
                return ReferenceRule.<String>of("").range(1, 2);
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
            public ReferenceRule<String> expectedRule() {
                return ReferenceRule.<String>of("");
            }

        }

    }

}
