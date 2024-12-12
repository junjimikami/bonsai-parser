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

/**
 * 
 * @author Junji Mikami
 */
interface ParseExceptionTestCase {

    Executable createTarget(Grammar grammar, Reader reader);

    @Test
    @DisplayName("Grammar [No matching production rule]")
    default void grammarInCaseOfNoMatchingProductionRule(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", PatternRule.of("1"))
                .add("S", PatternRule.of("2"))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Short-circuit grammar [No matching production rule]")
    default void shortCircuitGrammarInCaseOfNoMatchingProductionRule(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", PatternRule.of("1"))
                .add("S", PatternRule.of("2"))
                .shortCircuit();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Short-circuit grammar [No matching production rule 2]")
    default void shortCircuitGrammarInCaseOfNoMatchingProductionRule2(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(PatternRule.of("0"))
                        .add(ChoiceRule.builder()
                                .add(PatternRule.of("8"))
                                .add(PatternRule.of("9"))))
                .shortCircuit();
        var target = createTarget(grammar, new StringReader("01"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Short-circuit grammar [No matching production rule 3]")
    default void shortCircuitGrammarInCaseOfNoMatchingProductionRule3(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(PatternRule.of("0"))
                        .add(ChoiceRule.builder()
                                .add(PatternRule.of("8").opt())
                                .add(PatternRule.of("9").opt()))
                        .add(PatternRule.of("3")))
                .shortCircuit();
        var target = createTarget(grammar, new StringReader("03"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Short-circuit grammar [No matching production rule 4]")
    default void shortCircuitGrammarInCaseOfNoMatchingProductionRule4(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(PatternRule.of("0"))
                        .add(ChoiceRule.builder()
                                .add(PatternRule.of("8"))
                                .add(PatternRule.of(".")))
                        .add(PatternRule.of("3")))
                .shortCircuit();
        var target = createTarget(grammar, new StringReader("083"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Short-circuit grammar [No matching production rule 5]")
    default void shortCircuitGrammarInCaseOfNoMatchingProductionRule5(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(PatternRule.of("0").skip())
                        .add(PatternRule.of("1").atLeast(2)))
                .shortCircuit();
        var target = createTarget(grammar, new StringReader("01"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Short-circuit grammar [No matching production rule 6]")
    default void shortCircuitGrammarInCaseOfNoMatchingProductionRule6(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(PatternRule.of("0"))
                        .add(Rule.EMPTY))
                .shortCircuit();
        var target = createTarget(grammar, new StringReader("01"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Grammar [Ambiguous grammar]")
    default void grammarInCaseOfAmbiguousGrammar(TestReporter testReporter) throws Exception {
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
    default void choiceRuleInCaseOfNoMatchingRules(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(PatternRule.of("0"))
                        .add(ChoiceRule.builder()
                                .add(PatternRule.of("1"))
                                .add(PatternRule.of("2"))))
                .build();
        var target = createTarget(grammar, new StringReader("03"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Choice rule [No matching rules 2]")
    default void choiceRuleInCaseOfNoMatchingRules2(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(PatternRule.of("0"))
                        .add(ChoiceRule.builder()
                                .add(PatternRule.of("8").opt())
                                .add(PatternRule.of("9").opt()))
                        .add(PatternRule.of("3")))
                .build();
        var target = createTarget(grammar, new StringReader("03"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Short-circuit choice rule [No matching rules]")
    default void shortCircuitChoiceRuleInCaseOfNoMatchingRules(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(PatternRule.of("0"))
                        .add(ChoiceRule.builder()
                                .add(PatternRule.of("1"))
                                .add(PatternRule.of("2"))
                                .shortCircuit()))
                .build();
        var target = createTarget(grammar, new StringReader("03"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Choice rule [Ambiguous choice]")
    default void choiceRuleInCaseOfAmbiguousChoice(TestReporter testReporter) throws Exception {
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
    default void sequenceRuleInCaseOfNoMatchingRules(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(PatternRule.of("1"))
                        .add(SequenceRule.builder()
                                .add(PatternRule.of("2"))
                                .add(PatternRule.of("3"))))
                .build();
        var target = createTarget(grammar, new StringReader("13"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Pattern rule [No matching rules]")
    default void patternRuleInCaseOfNoMatchingRules(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(PatternRule.of("0"))
                        .add(PatternRule.of("1")))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Quantifier rule [Match Count Shortage]")
    default void quantifierRuleInCaseOfMatchCountShortage(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", PatternRule.of("0").atLeast(2))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Empty rule [No matching rules]")
    default void emptyRuleInCaseOfNoMatchingRules(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(PatternRule.of("0"))
                        .add(Rule.EMPTY))
                .build();
        var target = createTarget(grammar, new StringReader("01"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("Skip rule [No matching rules]")
    default void skipRuleInCaseOfNoMatchingRules(TestReporter testReporter) throws Exception {
        var grammar = SingleOriginGrammar.builder()
                .add("S", SequenceRule.builder()
                        .add(PatternRule.of("\n").skip())
                        .add(PatternRule.of("\r").skip())
                        .add(PatternRule.of("\r").skip())
                        .add(PatternRule.of("\n").skip())
                        .add(PatternRule.of("0"))
                        .add(PatternRule.of("2").skip()))
                .build();
        var target = createTarget(grammar, new StringReader("\n\r\r\n01"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry(ex.getMessage());
    }

}
