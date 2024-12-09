package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * 
 * @author Junji Mikami
 */
class SequenceRuleTest {

    @Test
    @DisplayName("of(Rule...) [Null parameter]")
    void ofInCaseOfNullParameter(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> SequenceRule.of((Rule[]) null));
        testReporter.publishEntry(ex.getMessage());
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("of(Rule...)")
    void of(List<Rule> rules) throws Exception {
        var rule = SequenceRule.of(rules.toArray(Rule[]::new));

        assertEquals(Rule.Kind.SEQUENCE, rule.getKind());
        assertIterableEquals(rules, rule.getRules());
    }

    static Stream<List<Rule>> of() {
        return Stream.of(
                List.of(),
                List.of(mock(Rule.class)),
                List.of(mock(Rule.class), mock(Rule.class)));
    }

    @Nested
    class TestCase1 implements SequenceRuleTestCase {

        @Override
        public SequenceRule createTarget() {
            return SequenceRule.of();
        }

        @Override
        public List<? extends Rule> expectedRules() {
            return List.of();
        }

    }

    @Nested
    class TestCase2 implements SequenceRuleTestCase {

        List<Rule> testData = List.of(mock(Rule.class));

        @Override
        public SequenceRule createTarget() {
            return SequenceRule.of(testData.toArray(Rule[]::new));
        }

        @Override
        public List<? extends Rule> expectedRules() {
            return testData;
        }

    }

    @Nested
    class TestCase3 implements SequenceRuleTestCase {

        List<Rule> testData = List.of(mock(Rule.class), mock(Rule.class));

        @Override
        public SequenceRule createTarget() {
            return SequenceRule.of(testData.toArray(Rule[]::new));
        }

        @Override
        public List<? extends Rule> expectedRules() {
            return testData;
        }

    }

    @Nested
    class BuilderTestCase1 implements SequenceRuleTestCase.BuilderTestCase {

        @Override
        public SequenceRule.Builder createTarget() {
            return SequenceRule.builder();
        }

        @Override
        public SequenceRule expectedRule() {
            return SequenceRule.of();
        }

    }

    @Nested
    class BuilderTestCase2 implements SequenceRuleTestCase.BuilderTestCase {

        List<Rule> testData = List.of(mock(Rule.class));

        @Override
        public SequenceRule.Builder createTarget() {
            var builder = SequenceRule.builder();
            testData.forEach(builder::add);
            return builder;
        }

        @Override
        public SequenceRule expectedRule() {
            return SequenceRule.of(testData.toArray(Rule[]::new));
        }

    }

    @Nested
    class BuilderTestCase3 implements SequenceRuleTestCase.BuilderTestCase {

        List<Rule> testData = List.of(mock(Rule.class), mock(Rule.class));

        @Override
        public SequenceRule.Builder createTarget() {
            var builder = SequenceRule.builder();
            testData.forEach(builder::add);
            return builder;
        }

        @Override
        public SequenceRule expectedRule() {
            return SequenceRule.of(testData.toArray(Rule[]::new));
        }

    }

}
