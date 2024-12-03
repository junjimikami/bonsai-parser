package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    default void noMatchingProductionRule() throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", PatternRule.of("1"))
                .add("S", PatternRule.of("2"))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        assertThrows(ParseException.class, target);
    }

    @Test
    @DisplayName("Grammar [Ambiguous grammar]")
    default void ambiguousGrammar() throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", PatternRule.of("1"))
                .add("S", PatternRule.of("."))
                .build();
        var target = createTarget(grammar, new StringReader("1"));

        assertThrows(ParseException.class, target);
    }

    @Test
    @DisplayName("Choice rule [No matching rules]")
    default void noMatchingRulesInChoiceRule() throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", ChoiceRule.builder()
                        .add(() -> PatternRule.of("1"))
                        .add(() -> PatternRule.of("2")))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        assertThrows(ParseException.class, target);
    }

    @Test
    @DisplayName("Choice rule [Ambiguous choice]")
    default void ambiguousChoiceInChoiceRule() throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", ChoiceRule.builder()
                        .add(() -> PatternRule.of("1"))
                        .add(() -> PatternRule.of(".")))
                .build();
        var target = createTarget(grammar, new StringReader("1"));

        assertThrows(ParseException.class, target);
    }

    @Test
    @DisplayName("Sequence rule [No matching rules]")
    default void noMatchingRulesInSequenceRule() throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(() -> PatternRule.of("1"))
                        .add(() -> PatternRule.of("2")))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        assertThrows(ParseException.class, target);
    }

    @Test
    @DisplayName("Pattern rule [No matching rules]")
    default void noMatchingRulesInPatternRule() throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", PatternRule.of("1"))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        assertThrows(ParseException.class, target);
    }

    @Test
    @DisplayName("Quantifier rule [Match Count Shortage]")
    default void matchCountShortageInQuantifierRule() throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", PatternRule.of("0").atLeast(2))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        assertThrows(ParseException.class, target);
    }

    @Test
    @DisplayName("Empty rule [No matching rules]")
    default void noMatchingRulesInEmptyRule() throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", Rule.EMPTY)
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        assertThrows(ParseException.class, target);
    }

}
