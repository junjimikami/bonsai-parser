package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Set;
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
class ChoiceRuleTest {

    @Test
    @DisplayName("of(Rule...) [Null parameter]")
    void ofInCaseOfNullParameter(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> ChoiceRule.of((Rule[]) null));
        testReporter.publishEntry(ex.getMessage());
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("of(Rule...)")
    void of(Set<Rule> choices) throws Exception {
        var rule = ChoiceRule.of(choices.toArray(Rule[]::new));

        assertEquals(Rule.Kind.CHOICE, rule.getKind());
        assertEquals(choices, rule.getChoices());
    }

    static Stream<Set<Rule>> of() {
        return Stream.of(
                Set.of(),
                Set.of(mock(Rule.class)),
                Set.of(mock(Rule.class), mock(Rule.class)));
    }

    @Nested
    class TestCase1 implements ChoiceRuleTestCase {

        @Override
        public ChoiceRule createTarget() {
            return ChoiceRule.of();
        }

        @Override
        public Set<? extends Rule> expectedChoices() {
            return Set.of();
        }

    }

    @Nested
    class TestCase2 implements ChoiceRuleTestCase {

        Set<Rule> testData = Set.of(mock(Rule.class));

        @Override
        public ChoiceRule createTarget() {
            return ChoiceRule.of(testData.toArray(Rule[]::new));
        }

        @Override
        public Set<? extends Rule> expectedChoices() {
            return testData;
        }

    }

    @Nested
    class TestCase3 implements ChoiceRuleTestCase {

        Set<Rule> testData = Set.of(mock(Rule.class), mock(Rule.class));

        @Override
        public ChoiceRule createTarget() {
            return ChoiceRule.of(testData.toArray(Rule[]::new));
        }

        @Override
        public Set<? extends Rule> expectedChoices() {
            return testData;
        }

    }

    @Nested
    class BuilderTestCase1 implements ChoiceRuleTestCase.BuilderTestCase {

        @Override
        public ChoiceRule.Builder createTarget() {
            return ChoiceRule.builder();
        }

        @Override
        public ChoiceRule expectedRule() {
            return ChoiceRule.of();
        }
    }

    @Nested
    class BuilderTestCase2 implements ChoiceRuleTestCase.BuilderTestCase {

        List<Rule> testData = List.of(mock(Rule.class));

        @Override
        public ChoiceRule.Builder createTarget() {
            var builder = ChoiceRule.builder();
            testData.forEach(builder::add);
            return builder;
        }

        @Override
        public ChoiceRule expectedRule() {
            return ChoiceRule.of(testData.toArray(Rule[]::new));
        }
    }

    @Nested
    class BuilderTestCase3 implements ChoiceRuleTestCase.BuilderTestCase {

        List<Rule> testData = List.of(mock(Rule.class), mock(Rule.class));

        @Override
        public ChoiceRule.Builder createTarget() {
            var builder = ChoiceRule.builder();
            testData.forEach(builder::add);
            return builder;
        }

        @Override
        public ChoiceRule expectedRule() {
            return ChoiceRule.of(testData.toArray(Rule[]::new));
        }
    }

}
