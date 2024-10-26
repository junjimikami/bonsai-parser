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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ChoiceRuleTest {

    @Test
    @DisplayName("of(Rule...) [Null parameter]")
    void ofInCaseOfNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> ChoiceRule.of((Rule[]) null));
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("of(Rule...)")
    void of(List<Rule> choices) throws Exception {
        var rule = ChoiceRule.of(choices.toArray(Rule[]::new));

        assertEquals(Rule.Kind.CHOICE, rule.getKind());
        assertIterableEquals(choices, rule.getChoices());
    }

    static Stream<List<Rule>> of() {
        return Stream.of(
                List.of(),
                List.of(mock(Rule.class)),
                List.of(mock(Rule.class), mock(Rule.class)));
    }

    @Nested
    class TestCase1 implements ChoiceRuleTestCase {

        @Override
        public ChoiceRule createTarget() {
            return ChoiceRule.of();
        }

        @Override
        public List<? extends Rule> expectedChoices() {
            return List.of();
        }

    }

    @Nested
    class TestCase2 implements ChoiceRuleTestCase {

        List<Rule> testData = List.of(mock(Rule.class));

        @Override
        public ChoiceRule createTarget() {
            return ChoiceRule.of(testData.toArray(Rule[]::new));
        }

        @Override
        public List<? extends Rule> expectedChoices() {
            return testData;
        }

    }

    @Nested
    class TestCase3 implements ChoiceRuleTestCase {

        List<Rule> testData = List.of(mock(Rule.class), mock(Rule.class));

        @Override
        public ChoiceRule createTarget() {
            return ChoiceRule.of(testData.toArray(Rule[]::new));
        }

        @Override
        public List<? extends Rule> expectedChoices() {
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
