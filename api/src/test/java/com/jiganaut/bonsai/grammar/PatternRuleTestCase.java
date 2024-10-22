package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.jiganaut.bonsai.grammar.Rule.Kind;

class PatternRuleTestCase implements RuleTestCase, QuantifiableTestCase, SkippableTestCase {

    @Override
    public PatternRule createTarget() {
        return PatternRule.of("");
    }

    @Override
    public Kind expectedKind() {
        return Kind.PATTERN;
    }

    @Override
    public RuleVisitor<Object[], String> createVisitor() {
        return new TestRuleVisitor<Object[], String>() {
            @Override
            public Object[] visitPattern(PatternRule pattern, String p) {
                return new Object[] { pattern, p };
            }
        };
    }

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

    @Test
    @DisplayName("of(st:String)")
    void ofSt() throws Exception {
        PatternRule.of("");
    }

    @Test
    @DisplayName("of(pa:Pattern)")
    void ofPa() throws Exception {
        PatternRule.of(Pattern.compile(""));
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = { "test", "[0-9]" })
    @DisplayName("getPattern()")
    void getPattern(String regex) throws Exception {
        var pattern = PatternRule.of(regex);

        assertEquals(regex, pattern.getPattern().pattern());
    }
}
