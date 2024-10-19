package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RulesTest {

    @Test
    @DisplayName("pattern(st:String)")
    void patternSt() throws Exception {
        var expected = PatternRule.of("123");
        var actual = Rules.pattern("123");

        assertEquals(expected.getPattern().pattern(),
                actual.getPattern().pattern());
    }

    @Test
    @DisplayName("pattern(pa:Pattern)")
    void patternPa() throws Exception {
        var expected = PatternRule.of("123");
        var actual = Rules.pattern(Pattern.compile("123"));

        assertEquals(expected.getPattern().pattern(),
                actual.getPattern().pattern());
    }

    @Test
    @DisplayName("sequenceOf(String[])")
    void sequenceOf() throws Exception {
        var expected = SequenceRule.builder()
                .add(PatternRule.of("1"))
                .add(PatternRule.of("2"))
                .add(PatternRule.of("3"));
        var actual = Rules.of(
                Rules.pattern("1"),
                Rules.pattern("2"),
                Rules.pattern("3"));

        assertEquals(expected.build().getRules().toString(),
                actual.getRules().toString());
    }

    @Test
    @DisplayName("oneOf(String[])")
    void oneOf() throws Exception {
        var expected = ChoiceRule.builder()
                .add(PatternRule.of("1"))
                .add(PatternRule.of("2"))
                .add(PatternRule.of("3"));
        var actual = Rules.oneOf(
                Rules.pattern("1"),
                Rules.pattern("2"),
                Rules.pattern("3"));

        assertEquals(expected.build().getChoices().toString(),
                actual.getChoices().toString());
    }

    @Test
    @DisplayName("reference(String)")
    void reference() throws Exception {
        var expected = ReferenceRule.of("A");
        var actual = Rules.reference("A");

        assertEquals(expected.lookup(Stubs.DUMMY_PRODUCTION_SET).getSymbol(),
                actual.lookup(Stubs.DUMMY_PRODUCTION_SET).getSymbol());
    }

    @Test
    @DisplayName("quote(String)")
    void quote() throws Exception {
        var expected = PatternRule.of(Pattern.quote("***..."));
        var actual = Rules.quote("***...");

        assertEquals(expected.getPattern().pattern(),
                actual.getPattern().pattern());
    }
}
