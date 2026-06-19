package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.MockFactory.mockRule;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Nested;

/**
 *
 * @author Junji Mikami
 */
class ChoiceRuleTest {

    @Nested
    class TestCase1 implements ChoiceRuleTestCase<String> {

        @Override
        public ChoiceRule<String> createTarget() {
            return ChoiceRule.<String>builder().build();
        }

        @Override
        public Set<Rule<String>> expectedChoices() {
            return Set.of();
        }

    }

    @Nested
    class TestCase2 implements ChoiceRuleTestCase<String> {

        Set<Rule<String>> testData = Set.of(mockRule());

        @Override
        public ChoiceRule<String> createTarget() {
            var builder = ChoiceRule.<String>builder();
            testData.forEach(builder::add);
            return builder.build();
        }

        @Override
        public Set<Rule<String>> expectedChoices() {
            return testData;
        }

    }

    @Nested
    class TestCase3 implements ChoiceRuleTestCase<String> {

        Set<Rule<String>> testData = Set.of(mockRule(), mockRule());

        @Override
        public ChoiceRule<String> createTarget() {
            var builder = ChoiceRule.<String>builder();
            testData.forEach(builder::add);
            return builder.build();
        }

        @Override
        public Set<Rule<String>> expectedChoices() {
            return testData;
        }

    }

    @Nested
    class TestCase4 implements ChoiceRuleTestCase<String> {

        @Override
        public ChoiceRule<String> createTarget() {
            return ChoiceRule.<String>builder().asShortCircuit().build();
        }

        @Override
        public Set<Rule<String>> expectedChoices() {
            return Set.of();
        }

        @Override
        public boolean expectedShortCircuit() {
            return true;
        }
    }

    @Nested
    class BuilderTestCase1 implements ChoiceRuleTestCase.BuilderTestCase<String> {

        @Override
        public ChoiceRule.Builder<String> createTarget() {
            return ChoiceRule.<String>builder();
        }

        @Override
        public ChoiceRule<String> expectedRule() {
            return ChoiceRule.<String>builder().build();
        }
    }

    @Nested
    class BuilderTestCase2 implements ChoiceRuleTestCase.BuilderTestCase<String> {

        List<Rule<String>> testData = List.of(mockRule());

        @Override
        public ChoiceRule.Builder<String> createTarget() {
            var builder = ChoiceRule.<String>builder();
            testData.forEach(builder::add);
            return builder;
        }

        @Override
        public ChoiceRule<String> expectedRule() {
            var builder = ChoiceRule.<String>builder();
            testData.forEach(builder::add);
            return builder.build();
        }
    }

    @Nested
    class BuilderTestCase3 implements ChoiceRuleTestCase.BuilderTestCase<String> {

        List<Rule<String>> testData = List.of(mockRule(), mockRule());

        @Override
        public ChoiceRule.Builder<String> createTarget() {
            var builder = ChoiceRule.<String>builder();
            testData.forEach(builder::add);
            return builder;
        }

        @Override
        public ChoiceRule<String> expectedRule() {
            var builder = ChoiceRule.<String>builder();
            testData.forEach(builder::add);
            return builder.build();
        }
    }

    @Nested
    class BuilderTestCase4 implements ChoiceRuleTestCase.BuilderTestCase<String> {

        @Override
        public ChoiceRule.Builder<String> createTarget() {
            return ChoiceRule.<String>builder().asShortCircuit();
        }

        @Override
        public ChoiceRule<String> expectedRule() {
            return ChoiceRule.<String>builder().asShortCircuit().build();
        }

        @Override
        public boolean expectedShortCircuit() {
            return true;
        }

    }

}
