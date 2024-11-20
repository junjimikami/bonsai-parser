package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ShortCircuitChoiceRuleTest {

    private static Set<Rule> setOf(Rule... args) {
        return new LinkedHashSet<>(Arrays.asList(args));
    }

    @Test
    @DisplayName("of(Rule...) [Null parameter]")
    void ofInCaseOfNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> ShortCircuitChoiceRule.of((Rule[]) null));
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("of(Rule...)")
    void of(Set<Rule> choices) throws Exception {
        var rule = ShortCircuitChoiceRule.of(choices.toArray(Rule[]::new));

        assertEquals(Rule.Kind.SHORT_CIRCUIT_CHOICE, rule.getKind());
        assertIterableEquals(choices, rule.getChoices());
    }

    static Stream<Set<Rule>> of() {
        return Stream.of(
                setOf(),
                setOf(mock(Rule.class)),
                setOf(mock(Rule.class), mock(Rule.class)));
    }

    @Nested
    class TestCase1 implements ShortCircuitChoiceRuleTestCase {

        @Override
        public ShortCircuitChoiceRule createTarget() {
            return ShortCircuitChoiceRule.of();
        }

        @Override
        public Set<? extends Rule> expectedChoices() {
            return setOf();
        }

    }

    @Nested
    class TestCase2 implements ShortCircuitChoiceRuleTestCase {

        Set<Rule> testData = setOf(mock(Rule.class));

        @Override
        public ShortCircuitChoiceRule createTarget() {
            return ShortCircuitChoiceRule.of(testData.toArray(Rule[]::new));
        }

        @Override
        public Set<? extends Rule> expectedChoices() {
            return testData;
        }

    }

    @Nested
    class TestCase3 implements ShortCircuitChoiceRuleTestCase {

        Set<Rule> testData = setOf(mock(Rule.class), mock(Rule.class));

        @Override
        public ShortCircuitChoiceRule createTarget() {
            return ShortCircuitChoiceRule.of(testData.toArray(Rule[]::new));
        }

        @Override
        public Set<? extends Rule> expectedChoices() {
            return testData;
        }

    }

    @Nested
    class BuilderTestCase1 implements ShortCircuitChoiceRuleTestCase.BuilderTestCase {

        @Override
        public ShortCircuitChoiceRule.Builder createTarget() {
            return ShortCircuitChoiceRule.builder();
        }

        @Override
        public ShortCircuitChoiceRule expectedRule() {
            return ShortCircuitChoiceRule.of();
        }
    }

    @Nested
    class BuilderTestCase2 implements ShortCircuitChoiceRuleTestCase.BuilderTestCase {

        List<Rule> testData = List.of(mock(Rule.class));

        @Override
        public ShortCircuitChoiceRule.Builder createTarget() {
            var builder = ShortCircuitChoiceRule.builder();
            testData.forEach(builder::add);
            return builder;
        }

        @Override
        public ShortCircuitChoiceRule expectedRule() {
            return ShortCircuitChoiceRule.of(testData.toArray(Rule[]::new));
        }
    }

    @Nested
    class BuilderTestCase3 implements ShortCircuitChoiceRuleTestCase.BuilderTestCase {

        List<Rule> testData = List.of(mock(Rule.class), mock(Rule.class));

        @Override
        public ShortCircuitChoiceRule.Builder createTarget() {
            var builder = ShortCircuitChoiceRule.builder();
            testData.forEach(builder::add);
            return builder;
        }

        @Override
        public ShortCircuitChoiceRule expectedRule() {
            return ShortCircuitChoiceRule.of(testData.toArray(Rule[]::new));
        }
    }

}
