package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RulesTest {

    @Test
    @DisplayName("pattern(st:String)")
    void patternSt() throws Exception {
        var expected = PatternRule.builder("123");
        var actual = Rules.pattern("123");

        assertEquals(expected.build(Stubs.DUMMY_PRODUCTION_SET).getPattern().pattern(),
                actual.build(Stubs.DUMMY_PRODUCTION_SET).getPattern().pattern());
    }

    @Test
    @DisplayName("pattern(pa:Pattern)")
    void patternPa() throws Exception {
        var expected = PatternRule.builder("123");
        var actual = Rules.pattern(Pattern.compile("123"));

        assertEquals(expected.build(Stubs.DUMMY_PRODUCTION_SET).getPattern().pattern(),
                actual.build(Stubs.DUMMY_PRODUCTION_SET).getPattern().pattern());
    }

    @Test
    @DisplayName("patternsOf(String[])")
    void patternsOf() throws Exception {
        var expected = SequenceRule.builder()
                .add(PatternRule.builder("1"))
                .add(PatternRule.builder("2"))
                .add(PatternRule.builder("3"));
        var actual = Rules.ofPatterns("1", "2", "3");

        assertEquals(expected.build(Stubs.DUMMY_PRODUCTION_SET).getRules().toString(),
                actual.build(Stubs.DUMMY_PRODUCTION_SET).getRules().toString());
    }

    @Test
    @DisplayName("oneOfPatterns(String[])")
    void oneOfPatterns() throws Exception {
        var expected = ChoiceRule.builder()
                .add(PatternRule.builder("1"))
                .add(PatternRule.builder("2"))
                .add(PatternRule.builder("3"));
        var actual = Rules.oneOfPatterns("1", "2", "3");

        assertEquals(expected.build(Stubs.DUMMY_PRODUCTION_SET).getChoices().toString(),
                actual.build(Stubs.DUMMY_PRODUCTION_SET).getChoices().toString());
    }

    @Test
    @DisplayName("sequenceBuilder()")
    void sequenceBuilder() throws Exception {
        var expected = SequenceRule.builder()
                .add(PatternRule.builder("1"))
                .add(PatternRule.builder("2"))
                .add(PatternRule.builder("3"));
        var actual = Rules.sequenceBuilder()
                .add(PatternRule.builder("1"))
                .add(PatternRule.builder("2"))
                .add(PatternRule.builder("3"));

        assertEquals(expected.build(Stubs.DUMMY_PRODUCTION_SET).getRules().toString(),
                actual.build(Stubs.DUMMY_PRODUCTION_SET).getRules().toString());
    }

    @Test
    @DisplayName("sequenceOf(String[])")
    void sequenceOf() throws Exception {
        var expected = SequenceRule.builder()
                .add(PatternRule.builder("1"))
                .add(PatternRule.builder("2"))
                .add(PatternRule.builder("3"));
        var actual = Rules.of(
                Rules.pattern("1"),
                Rules.pattern("2"),
                Rules.pattern("3"));

        assertEquals(expected.build(Stubs.DUMMY_PRODUCTION_SET).getRules().toString(),
                actual.build(Stubs.DUMMY_PRODUCTION_SET).getRules().toString());
    }

    @Test
    @DisplayName("choiceBuilder()")
    void choiceBuilder() throws Exception {
        var expected = ChoiceRule.builder()
                .add(PatternRule.builder("1"))
                .add(PatternRule.builder("2"))
                .add(PatternRule.builder("3"));
        var actual = Rules.choiceBuilder()
                .add(PatternRule.builder("1"))
                .add(PatternRule.builder("2"))
                .add(PatternRule.builder("3"));

        assertEquals(expected.build(Stubs.DUMMY_PRODUCTION_SET).getChoices().toString(),
                actual.build(Stubs.DUMMY_PRODUCTION_SET).getChoices().toString());
    }

    @Test
    @DisplayName("oneOf(String[])")
    void oneOf() throws Exception {
        var expected = ChoiceRule.builder()
                .add(PatternRule.builder("1"))
                .add(PatternRule.builder("2"))
                .add(PatternRule.builder("3"));
        var actual = Rules.oneOf(
                Rules.pattern("1"),
                Rules.pattern("2"),
                Rules.pattern("3"));

        assertEquals(expected.build(Stubs.DUMMY_PRODUCTION_SET).getChoices().toString(),
                actual.build(Stubs.DUMMY_PRODUCTION_SET).getChoices().toString());
    }

    @Test
    @DisplayName("reference(String)")
    void reference() throws Exception {
        var expected = ReferenceRule.builder("A");
        var actual = Rules.reference("A");

        assertEquals(expected.build(Stubs.DUMMY_PRODUCTION_SET).getProduction().getSymbol(),
                actual.build(Stubs.DUMMY_PRODUCTION_SET).getProduction().getSymbol());
    }

    @Test
    @DisplayName("referencesOf(String[])")
    void referencesOf() throws Exception {
        var expected = SequenceRule.builder()
                .add(ReferenceRule.builder("A"))
                .add(ReferenceRule.builder("B"))
                .add(ReferenceRule.builder("C"));
        var actual = Rules.ofReferences("A", "B", "C");

        assertEquals(expected.build(Stubs.DUMMY_PRODUCTION_SET).getRules().toString(),
                actual.build(Stubs.DUMMY_PRODUCTION_SET).getRules().toString());
    }

    @Test
    @DisplayName("oneOfreferences(String[])")
    void oneOfreferences() throws Exception {
        var expected = ChoiceRule.builder()
                .add(ReferenceRule.builder("A"))
                .add(ReferenceRule.builder("B"))
                .add(ReferenceRule.builder("C"));
        var actual = Rules.oneOfreferences("A", "B", "C");

        assertEquals(expected.build(Stubs.DUMMY_PRODUCTION_SET).getChoices().toString(),
                actual.build(Stubs.DUMMY_PRODUCTION_SET).getChoices().toString());
    }

    @Test
    @DisplayName("quote(String)")
    void quote() throws Exception {
        var expected = PatternRule.builder(Pattern.quote("***..."));
        var actual = Rules.quote("***...");

        assertEquals(expected.build(Stubs.DUMMY_PRODUCTION_SET).getPattern().pattern(),
                actual.build(Stubs.DUMMY_PRODUCTION_SET).getPattern().pattern());
    }
}
