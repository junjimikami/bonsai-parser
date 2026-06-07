package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.GrammarMockFactory.mockRule;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Junji Mikami
 */
class SequenceRuleTest {

    @Test
    @DisplayName("builder()")
    void builder() throws Exception {
        var target = SequenceRule.<String>builder();

        assertNotNull(target);
        assertInstanceOf(SequenceRule.Builder.class, target);
    }

    @Nested
    class TestCase1 implements SequenceRuleTestCase<String> {

        @Override
        public SequenceRule<String> createTarget() {
            return SequenceRule.<String>builder().build();
        }

        @Override
        public List<? extends Rule<String>> expectedRules() {
            return List.of();
        }

    }

    @Nested
    class TestCase2 implements SequenceRuleTestCase<String> {

        List<Rule<String>> testData = List.of(mockRule());

        @Override
        public SequenceRule<String> createTarget() {
            var builder = SequenceRule.<String>builder();
            testData.forEach(builder::add);
            return builder.build();
        }

        @Override
        public List<? extends Rule<String>> expectedRules() {
            return testData;
        }

    }

    @Nested
    class TestCase3 implements SequenceRuleTestCase<String> {

        List<Rule<String>> testData = List.of(mockRule(), mockRule());

        @Override
        public SequenceRule<String> createTarget() {
            var builder = SequenceRule.<String>builder();
            testData.forEach(builder::add);
            return builder.build();
        }

        @Override
        public List<? extends Rule<String>> expectedRules() {
            return testData;
        }

    }

    @Nested
    class BuilderTestCase1 implements SequenceRuleTestCase.BuilderTestCase<String> {

        @Override
        public SequenceRule.Builder<String> createTarget() {
            return SequenceRule.<String>builder();
        }

        @Override
        public SequenceRule<String> expectedRule() {
            return SequenceRule.<String>builder().build();
        }

    }

    @Nested
    class BuilderTestCase2 implements SequenceRuleTestCase.BuilderTestCase<String> {

        List<Rule<String>> testData = List.of(mockRule());

        @Override
        public SequenceRule.Builder<String> createTarget() {
            var builder = SequenceRule.<String>builder();
            testData.forEach(builder::add);
            return builder;
        }

        @Override
        public SequenceRule<String> expectedRule() {
            var builder = SequenceRule.<String>builder();
            testData.forEach(builder::add);
            return builder.build();
        }

    }

    @Nested
    class BuilderTestCase3 implements SequenceRuleTestCase.BuilderTestCase<String> {

        List<Rule<String>> testData = List.of(mockRule(), mockRule());

        @Override
        public SequenceRule.Builder<String> createTarget() {
            var builder = SequenceRule.<String>builder();
            testData.forEach(builder::add);
            return builder;
        }

        @Override
        public SequenceRule<String> expectedRule() {
            var builder = SequenceRule.<String>builder();
            testData.forEach(builder::add);
            return builder.build();
        }

    }

}
