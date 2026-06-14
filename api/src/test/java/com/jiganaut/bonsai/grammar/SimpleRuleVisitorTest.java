package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.MockFactory.mockChoiceRule;
import static com.jiganaut.bonsai.grammar.MockFactory.mockEmptyRule;
import static com.jiganaut.bonsai.grammar.MockFactory.mockMatchingRule;
import static com.jiganaut.bonsai.grammar.MockFactory.mockProductionRule;
import static com.jiganaut.bonsai.grammar.MockFactory.mockQuantifierRule;
import static com.jiganaut.bonsai.grammar.MockFactory.mockReferenceRule;
import static com.jiganaut.bonsai.grammar.MockFactory.mockSequenceRule;
import static com.jiganaut.bonsai.grammar.MockFactory.mockSkipRule;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Junji Mikami
 */
class SimpleRuleVisitorTest implements RuleVisitorTestCase<String, String, String> {

    private static final class TestVisitor implements SimpleRuleVisitor<String, String, String> {

        @Override
        public String defaultAction(Rule<String> rule, String p) {
            return "%s:%s".formatted(rule.getKind(), p);
        }

    }

    @Override
    public RuleVisitor<String, String, String> createTarget() {
        return new TestVisitor();
    }

    @Override
    public String createParameter() {
        return "test";
    }

    @Test
    @DisplayName("visitChoice(ch:ChoiceRule, p:P)")
    void visitChoice() throws Exception {
        var visitor = createTarget();
        ChoiceRule<String> choice = mockChoiceRule();

        assertEquals("CHOICE:test", visitor.visitChoice(choice, createParameter()));
    }

    @Test
    @DisplayName("visitChoiceAsShortCircuit(ch:ChoiceRule, p:P)")
    void visitChoiceAsShortCircuit() throws Exception {
        var visitor = createTarget();
        ChoiceRule<String> choice = mockChoiceRule();

        assertEquals("CHOICE:test", visitor.visitChoiceAsShortCircuit(choice, createParameter()));
    }

    @Test
    @DisplayName("visitSequence(se:SequenceRule, p:P)")
    void visitSequence() throws Exception {
        var visitor = createTarget();
        SequenceRule<String> sequence = mockSequenceRule();

        assertEquals("SEQUENCE:test", visitor.visitSequence(sequence, createParameter()));
    }

    @Test
    @DisplayName("visitMatch(ma:MatchingRule, p:P)")
    void visitMatch() throws Exception {
        var visitor = createTarget();
        MatchingRule<String> match = mockMatchingRule();

        assertEquals("MATCH:test", visitor.visitMatch(match, createParameter()));
    }

    @Test
    @DisplayName("visitReference(re:ReferenceRule, p:P)")
    void visitReference() throws Exception {
        var visitor = createTarget();
        ReferenceRule<String> reference = mockReferenceRule();

        assertEquals("REFERENCE:test", visitor.visitReference(reference, createParameter()));
    }

    @Test
    @DisplayName("visitQuantifier(qu:QuantifierRule, p:P)")
    void visitQuantifier() throws Exception {
        var visitor = createTarget();
        QuantifierRule<String> quantifier = mockQuantifierRule();

        assertEquals("QUANTIFIER:test", visitor.visitQuantifier(quantifier, createParameter()));
    }

    @Test
    @DisplayName("visitSkip(sk:SkipRule, p:P)")
    void visitSkip() throws Exception {
        var visitor = createTarget();
        SkipRule<String> skip = mockSkipRule();

        assertEquals("SKIP:test", visitor.visitSkip(skip, createParameter()));
    }

    @Test
    @DisplayName("visitEmpty(em:EmptyRule, p:P)")
    void visitEmpty() throws Exception {
        var visitor = createTarget();
        EmptyRule<String> empty = mockEmptyRule();

        assertEquals("EMPTY:test", visitor.visitEmpty(empty, createParameter()));
    }

    @Test
    @DisplayName("visitProduction(pr:ProductionRule, p:P)")
    void visitProduction() throws Exception {
        var visitor = createTarget();
        ProductionRule<String> productionRule = mockProductionRule("S");

        assertEquals("PRODUCTION:test", visitor.visitProduction(productionRule, createParameter()));
    }

}