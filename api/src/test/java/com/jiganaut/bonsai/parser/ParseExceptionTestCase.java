package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.function.Executable;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.SingleOriginGrammar;

interface ParseExceptionTestCase {

    Executable createTarget(Grammar grammar, Reader reader);

    @Test
    @DisplayName("Grammar [No matching production rule]")
    default void noMatchingProductionRule(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", PatternRule.of("1"))
                .add("S", PatternRule.of("2"))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Grammar [Ambiguous grammar]")
    default void ambiguousGrammar(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", PatternRule.of("1"))
                .add("S", PatternRule.of("."))
                .build();
        var target = createTarget(grammar, new StringReader("1"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Choice rule [No matching rules]")
    default void noMatchingRulesInChoiceRule(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", ChoiceRule.builder()
                        .add(() -> PatternRule.of("1"))
                        .add(() -> PatternRule.of("2")))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Choice rule [Ambiguous choice]")
    default void ambiguousChoiceInChoiceRule(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", ChoiceRule.builder()
                        .add(() -> PatternRule.of("1"))
                        .add(() -> PatternRule.of(".")))
                .build();
        var target = createTarget(grammar, new StringReader("1"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Sequence rule [No matching rules]")
    default void noMatchingRulesInSequenceRule(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(() -> PatternRule.of("1"))
                        .add(() -> PatternRule.of("2")))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Pattern rule [No matching rules]")
    default void noMatchingRulesInPatternRule(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", PatternRule.of("1"))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Quantifier rule [Match Count Shortage]")
    default void matchCountShortageInQuantifierRule(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", PatternRule.of("0").atLeast(2))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Empty rule [No matching rules]")
    default void noMatchingRulesInEmptyRule(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", Rule.EMPTY)
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

}
