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
import com.jiganaut.bonsai.grammar.Rules;
import com.jiganaut.bonsai.grammar.SequenceRule;

/**
 *
 * @author Junji Mikami
 */
interface ParseExceptionTestCase {

    Executable createTarget(Grammar<String> grammar, Reader reader);

    @Test
    @DisplayName("throws ParseException when no production rule matches")
    default void throwsParseExceptionWhenNoProductionRuleMatches(TestReporter testReporter) throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", Rules.pattern("1"))
                .add("S", Rules.pattern("2"))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no production rule matches with short-circuit grammar")
    default void throwsParseExceptionWhenNoProductionRuleMatchesWithShortCircuitGrammar(TestReporter testReporter)
            throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", Rules.pattern("1"))
                .add("S", Rules.pattern("2"))
                .asShortCircuit()
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no production rule matches with short-circuit grammar (case 2)")
    default void throwsParseExceptionWhenNoProductionRuleMatchesWithShortCircuitGrammarCase2(TestReporter testReporter)
            throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", SequenceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(ChoiceRule.<String>builder()
                                .add(Rules.pattern("8"))
                                .add(Rules.pattern("9"))))
                .asShortCircuit()
                .build();
        var target = createTarget(grammar, new StringReader("01"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no production rule matches with short-circuit grammar (case 3)")
    default void throwsParseExceptionWhenNoProductionRuleMatchesWithShortCircuitGrammarCase3(TestReporter testReporter)
            throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", SequenceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(ChoiceRule.<String>builder()
                                .add(Rules.pattern("8").opt())
                                .add(Rules.pattern("9").opt()))
                        .add(Rules.pattern("3")))
                .asShortCircuit()
                .build();
        var target = createTarget(grammar, new StringReader("03"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no production rule matches with short-circuit grammar (case 4)")
    default void throwsParseExceptionWhenNoProductionRuleMatchesWithShortCircuitGrammarCase4(TestReporter testReporter)
            throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", SequenceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(ChoiceRule.<String>builder()
                                .add(Rules.pattern("8"))
                                .add(Rules.pattern(".")))
                        .add(Rules.pattern("3")))
                .asShortCircuit()
                .build();
        var target = createTarget(grammar, new StringReader("083"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no production rule matches with short-circuit grammar (case 5)")
    default void throwsParseExceptionWhenNoProductionRuleMatchesWithShortCircuitGrammarCase5(TestReporter testReporter)
            throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", SequenceRule.<String>builder()
                        .add(Rules.pattern("0").skip())
                        .add(Rules.pattern("1").atLeast(2)))
                .asShortCircuit()
                .build();
        var target = createTarget(grammar, new StringReader("01"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no production rule matches with short-circuit grammar (case 6)")
    default void throwsParseExceptionWhenNoProductionRuleMatchesWithShortCircuitGrammarCase6(TestReporter testReporter)
            throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", SequenceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(Rules.empty()))
                .asShortCircuit()
                .build();
        var target = createTarget(grammar, new StringReader("01"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when grammar is ambiguous")
    default void throwsParseExceptionWhenGrammarIsAmbiguous(TestReporter testReporter) throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", Rules.pattern("1"))
                .add("S", Rules.pattern("."))
                .build();
        var target = createTarget(grammar, new StringReader("1"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no rule matches in choice rule")
    default void throwsParseExceptionWhenNoRuleMatchesInChoiceRule(TestReporter testReporter) throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", SequenceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(ChoiceRule.<String>builder()
                                .add(Rules.pattern("1"))
                                .add(Rules.pattern("2"))))
                .build();
        var target = createTarget(grammar, new StringReader("03"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no rule matches in choice rule (case 2)")
    default void throwsParseExceptionWhenNoRuleMatchesInChoiceRuleCase2(TestReporter testReporter) throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", SequenceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(ChoiceRule.<String>builder()
                                .add(Rules.pattern("8").opt())
                                .add(Rules.pattern("9").opt()))
                        .add(Rules.pattern("3")))
                .build();
        var target = createTarget(grammar, new StringReader("03"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no rule matches in short-circuit choice rule")
    default void throwsParseExceptionWhenNoRuleMatchesInShortCircuitChoiceRule(TestReporter testReporter)
            throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", SequenceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(ChoiceRule.<String>builder()
                                .add(Rules.pattern("1"))
                                .add(Rules.pattern("2"))
                                .asShortCircuit()))
                .build();
        var target = createTarget(grammar, new StringReader("03"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when choice rule is ambiguous")
    default void throwsParseExceptionWhenChoiceRuleIsAmbiguous(TestReporter testReporter) throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", ChoiceRule.<String>builder()
                        .add(() -> Rules.pattern("1"))
                        .add(() -> Rules.pattern(".")))
                .build();
        var target = createTarget(grammar, new StringReader("1"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no rule matches in sequence rule")
    default void throwsParseExceptionWhenNoRuleMatchesInSequenceRule(TestReporter testReporter) throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", SequenceRule.<String>builder()
                        .add(Rules.pattern("1"))
                        .add(SequenceRule.<String>builder()
                                .add(Rules.pattern("2"))
                                .add(Rules.pattern("3"))))
                .build();
        var target = createTarget(grammar, new StringReader("13"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no rule matches in sequence rule (case 2)")
    default void throwsParseExceptionWhenNoRuleMatchesInSequenceRuleCase2(TestReporter testReporter) throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", SequenceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(Rules.pattern("1")))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when quantifier minimum is not met")
    default void throwsParseExceptionWhenQuantifierMinimumIsNotMet(TestReporter testReporter) throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", Rules.pattern("0").atLeast(2))
                .build();
        var target = createTarget(grammar, new StringReader("0"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no rule matches in empty rule")
    default void throwsParseExceptionWhenNoRuleMatchesInEmptyRule(TestReporter testReporter) throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", SequenceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(Rules.empty()))
                .build();
        var target = createTarget(grammar, new StringReader("01"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("throws ParseException when no rule matches in skip rule")
    default void throwsParseExceptionWhenNoRuleMatchesInSkipRule(TestReporter testReporter) throws Exception {
        var grammar = Grammar.<String>builder("S")
                .add("S", SequenceRule.<String>builder()
                        .add(Rules.pattern("\n").skip())
                        .add(Rules.pattern("\r").skip())
                        .add(Rules.pattern("\r").skip())
                        .add(Rules.pattern("\n").skip())
                        .add(Rules.pattern("0"))
                        .add(Rules.pattern("2").skip()))
                .build();
        var target = createTarget(grammar, new StringReader("\n\r\r\n01"));

        var ex = assertThrows(ParseException.class, target);
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

}
