package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
interface RuleTestCase extends TestCase {

    interface BuilderTestCase extends TestCase {
        Rule.Builder createTarget();

        Rule expectedRule();

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [Post-build operation]")
        default void buildInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.build());
            testReporter.publishEntry(ex.getMessage());
        }

        void build() throws Exception;

    }

    Rule createTarget();

    Kind expectedKind();

    RuleVisitor<Object[], String> testVisitor = new SimpleRuleVisitor<>() {

        @Override
        public Object[] visitChoice(ChoiceRule choice, String p) {
            assertEquals(Rule.Kind.CHOICE, choice.getKind());
            return SimpleRuleVisitor.super.visitChoice(choice, p);
        }

        @Override
        public Object[] visitSequence(SequenceRule sequence, String p) {
            assertEquals(Rule.Kind.SEQUENCE, sequence.getKind());
            return SimpleRuleVisitor.super.visitSequence(sequence, p);
        }

        @Override
        public Object[] visitPattern(PatternRule pattern, String p) {
            assertEquals(Rule.Kind.PATTERN, pattern.getKind());
            return SimpleRuleVisitor.super.visitPattern(pattern, p);
        }

        @Override
        public Object[] visitReference(ReferenceRule reference, String p) {
            assertEquals(Rule.Kind.REFERENCE, reference.getKind());
            return SimpleRuleVisitor.super.visitReference(reference, p);
        }

        @Override
        public Object[] visitQuantifier(QuantifierRule quantifier, String p) {
            assertEquals(Rule.Kind.QUANTIFIER, quantifier.getKind());
            return SimpleRuleVisitor.super.visitQuantifier(quantifier, p);
        }

        @Override
        public Object[] visitSkip(SkipRule skip, String p) {
            assertEquals(Rule.Kind.SKIP, skip.getKind());
            return SimpleRuleVisitor.super.visitSkip(skip, p);
        }

        @Override
        public Object[] visitEmpty(Rule empty, String p) {
            assertEquals(Rule.Kind.EMPTY, empty.getKind());
            return SimpleRuleVisitor.super.visitEmpty(empty, p);
        }

        @Override
        public Object[] defaultAction(Rule rule, String p) {
            return new Object[] { rule, p };
        }
    };

    @Test
    @DisplayName("objectMethods()")
    default void objectMethods() throws Exception {
        var rule = createTarget();

        rule.toString();
    }

    @Test
    @DisplayName("getKind()")
    default void getKind() throws Exception {
        var rule = createTarget();

        assertEquals(expectedKind(), rule.getKind());
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor) [Null parameter]")
    default void acceptEvInCaseOfNullParameter(TestReporter testReporter) throws Exception {
        var rule = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> rule.accept(null));
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("accept(rv:RuleVisitor)")
    default void acceptRv() throws Exception {
        var rule = createTarget();
        var visitor = testVisitor;
        var expected = new Object[] { rule, null };
        var result = rule.accept(visitor);
        var result2 = visitor.visit(rule);

        assertArrayEquals(expected, result);
        assertArrayEquals(expected, result2);
    }

    @DisplayName("accept(rv:RuleVisitor, pos:P)")
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { "test" })
    default void acceptRvP(String arg) throws Exception {
        var rule = createTarget();
        var visitor = testVisitor;
        var expected = new Object[] { rule, arg };
        var result = rule.accept(visitor, arg);
        var result2 = visitor.visit(rule, arg);

        assertArrayEquals(expected, result);
        assertArrayEquals(expected, result2);
    }
}
