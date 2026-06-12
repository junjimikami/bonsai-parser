package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.MockFactory.mockRule;
import static com.jiganaut.bonsai.grammar.MockFactory.mockRuleVisitor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.jiganaut.bonsai.TestCase;
import com.jiganaut.bonsai.grammar.Rule.Kind;

/**
 *
 * @author Junji Mikami
 */
interface RuleTestCase<T> extends TestCase {

    interface BuilderTestCase<T> extends TestCase {
        @Override
        Rule.Builder<T> createTarget();

        Rule<T> expectedRule();

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [Post-build]")
        default void buildWhenPostBuild(TestReporter testReporter) throws Exception {
            var target = createTarget();
            target.build();

            var ex = assertThrows(IllegalStateException.class, () -> target.build());
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        void build() throws Exception;

    }

    @Override
    Rule<T> createTarget();

    Kind expectedKind();

    @Test
    @DisplayName("equals(Object)")
    default void equals(TestReporter testReporter) throws Exception {
        var target = createTarget();

        assertFalse(target.equals(mockRule()));
        assertFalse(target.equals(null));
        assertTrue(target.equals(createTarget()));
    }

    @Test
    @DisplayName("hashCode()")
    default void hashCode(TestReporter testReporter) throws Exception {
        var target = createTarget();

        assertTrue(target.hashCode() == createTarget().hashCode());

        testReporter.publishEntry("hashCode()=%d".formatted(target.hashCode()));
    }

    @Test
    @DisplayName("toString()")
    default void toString(TestReporter testReporter) throws Exception {
        var target = createTarget();

        testReporter.publishEntry("toString()=\"%s\"".formatted(target.toString()));
    }

    @Test
    @DisplayName("getKind()")
    default void getKind() throws Exception {
        var target = createTarget();

        assertEquals(expectedKind(), target.getKind());
    }

    @Test
    @DisplayName("accept(rv:RuleVisitor) [rv == null]")
    default void acceptRvWhenRvIsNull(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> target.accept(null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("accept(rv:RuleVisitor)")
    default void acceptRv() throws Exception {
        var target = createTarget();
        RuleVisitor<T, Void, Void> visitor = mockRuleVisitor();

        target.accept(visitor);

        switch (target) {
            case ChoiceRule<T> choice when choice.isShortCircuit() -> verify(visitor).visitChoiceAsShortCircuit(choice, null);
            case ChoiceRule<T> choice -> verify(visitor).visitChoice(choice, null);
            case SequenceRule<T> sequence -> verify(visitor).visitSequence(sequence, null);
            case MatchingRule<T> match -> verify(visitor).visitMatch(match, null);
            case ReferenceRule<T> reference -> verify(visitor).visitReference(reference, null);
            case QuantifierRule<T> quantifier -> verify(visitor).visitQuantifier(quantifier, null);
            case SkipRule<T> skip -> verify(visitor).visitSkip(skip, null);
            case EmptyRule<T> empty -> verify(visitor).visitEmpty(empty, null);
            case ProductionRule<T> production -> verify(visitor).visitProduction(production, null);
            default -> throw new AssertionError();
        }
    }

    @DisplayName("accept(rv:RuleVisitor, p:P)")
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { "test" })
    default void acceptRvP(String arg) throws Exception {
        var target = createTarget();
        RuleVisitor<T, Void, String> visitor = mockRuleVisitor();

        target.accept(visitor, arg);

        switch (target) {
            case ChoiceRule<T> choice when choice.isShortCircuit() -> verify(visitor).visitChoiceAsShortCircuit(choice, arg);
            case ChoiceRule<T> choice -> verify(visitor).visitChoice(choice, arg);
            case SequenceRule<T> sequence -> verify(visitor).visitSequence(sequence, arg);
            case MatchingRule<T> match -> verify(visitor).visitMatch(match, arg);
            case ReferenceRule<T> reference -> verify(visitor).visitReference(reference, arg);
            case QuantifierRule<T> quantifier -> verify(visitor).visitQuantifier(quantifier, arg);
            case SkipRule<T> skip -> verify(visitor).visitSkip(skip, arg);
            case EmptyRule<T> empty -> verify(visitor).visitEmpty(empty, arg);
            case ProductionRule<T> production -> verify(visitor).visitProduction(production, arg);
            default -> throw new AssertionError();
        }
    }
}
