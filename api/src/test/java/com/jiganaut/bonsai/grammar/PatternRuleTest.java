package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PatternRuleTest {

    @Test
    @DisplayName("of(st:String) [Null parameter]")
    void ofStInCaseOfNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> PatternRule.of((String) null));
    }

    @Test
    @DisplayName("of(pa:Pattern) [Null parameter]")
    void ofPaInCaseOfNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> PatternRule.of((Pattern) null));
    }

    @Test
    @DisplayName("of(st:String) [Invalid regex]")
    void ofStInCaseOfInvalidRegex() throws Exception {
        assertThrows(PatternSyntaxException.class, () -> PatternRule.of("["));
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = { "[0-9]" })
    @DisplayName("of(st:String)")
    void ofSt(String regex) throws Exception {
        var rule = PatternRule.of(regex);

        assertEquals(Rule.Kind.PATTERN, rule.getKind());
        assertEquals(regex, rule.getPattern().pattern());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = { "[0-9]" })
    @DisplayName("of(pa:Pattern)")
    void ofPa(String regex) throws Exception {
        var rule = PatternRule.of(Pattern.compile(regex));

        assertEquals(Rule.Kind.PATTERN, rule.getKind());
        assertEquals(regex, rule.getPattern().pattern());
    }

    @Nested
    class TestCase1 implements PatternRuleTestCase {

        @Override
        public PatternRule createTarget() {
            return PatternRule.of(expectedPattern());
        }

        @Override
        public String expectedPattern() {
            return "";
        }

    }

    @Nested
    class TestCase2 implements PatternRuleTestCase {

        @Override
        public PatternRule createTarget() {
            return PatternRule.of(expectedPattern());
        }

        @Override
        public String expectedPattern() {
            return "[0-9]";
        }

    }

}
