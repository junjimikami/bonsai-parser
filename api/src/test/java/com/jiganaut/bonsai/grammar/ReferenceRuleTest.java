package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * 
 * @author Junji Mikami
 */
class ReferenceRuleTest {

    @Test
    @DisplayName("of(String) [Null parameter]")
    void ofInCaseOfNullParameter(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> ReferenceRule.of(null));
        testReporter.publishEntry(ex.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = { " ", "1", "a", "[" })
    @DisplayName("of(String)")
    void of(String symbol) throws Exception {
        var rule = ReferenceRule.of(symbol);

        assertEquals(Rule.Kind.REFERENCE, rule.getKind());
        assertEquals(symbol, rule.getSymbol());
    }

    @Nested
    class TestCase1 implements ReferenceRuleTestCase {

        @Override
        public ReferenceRule createTarget() {
            return ReferenceRule.of(expectedSymbol());
        }

        @Override
        public String expectedSymbol() {
            return "";
        }

    }

    @Nested
    class TestCase2 implements ReferenceRuleTestCase {

        @Override
        public ReferenceRule createTarget() {
            return ReferenceRule.of(expectedSymbol());
        }

        @Override
        public String expectedSymbol() {
            return " ";
        }

    }

    @Nested
    class TestCase3 implements ReferenceRuleTestCase {

        @Override
        public ReferenceRule createTarget() {
            return ReferenceRule.of(expectedSymbol());
        }

        @Override
        public String expectedSymbol() {
            return "1";
        }

    }

    @Nested
    class TestCase4 implements ReferenceRuleTestCase {

        @Override
        public ReferenceRule createTarget() {
            return ReferenceRule.of(expectedSymbol());
        }

        @Override
        public String expectedSymbol() {
            return "a";
        }

    }

    @Nested
    class TestCase5 implements ReferenceRuleTestCase {

        @Override
        public ReferenceRule createTarget() {
            return ReferenceRule.of(expectedSymbol());
        }

        @Override
        public String expectedSymbol() {
            return "[";
        }

    }

}
