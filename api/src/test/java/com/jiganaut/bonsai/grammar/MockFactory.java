package com.jiganaut.bonsai.grammar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jiganaut.bonsai.parser.Token;

final class MockFactory {

    static <T> Grammar<T> mockGrammar() {
        @SuppressWarnings("unchecked")
        Grammar<T> grammar = mock(Grammar.class);
        return grammar;
    }

    static <T> ProductionRule<T> mockProductionRule(String symbol, Rule<T> rule) {
        @SuppressWarnings("unchecked")
        ProductionRule<T> productionRule = mock(ProductionRule.class);
        when(productionRule.getKind()).thenReturn(Rule.Kind.PRODUCTION);
        when(productionRule.getSymbol()).thenReturn(symbol);
        when(productionRule.getRule()).thenReturn(rule);
        return productionRule;
    }

    static <T> ProductionRule<T> mockProductionRule(String symbol) {
        @SuppressWarnings("unchecked")
        ProductionRule<T> productionRule = mock(ProductionRule.class);
        when(productionRule.getKind()).thenReturn(Rule.Kind.PRODUCTION);
        when(productionRule.getSymbol()).thenReturn(symbol);
        return productionRule;
    }

    static <T> Rule<T> mockRule() {
        @SuppressWarnings("unchecked")
        Rule<T> rule = mock(Rule.class);
        when(rule.getKind()).thenReturn(Rule.Kind.MATCH);
        return rule;
    }

    static <T> ChoiceRule<T> mockChoiceRule() {
        @SuppressWarnings("unchecked")
        ChoiceRule<T> rule = mock(ChoiceRule.class);
        when(rule.getKind()).thenReturn(Rule.Kind.CHOICE);
        return rule;
    }

    static <T> SequenceRule<T> mockSequenceRule() {
        @SuppressWarnings("unchecked")
        SequenceRule<T> rule = mock(SequenceRule.class);
        when(rule.getKind()).thenReturn(Rule.Kind.SEQUENCE);
        return rule;
    }

    static <T> MatchingRule<T> mockMatchingRule() {
        @SuppressWarnings("unchecked")
        MatchingRule<T> rule = mock(MatchingRule.class);
        when(rule.getKind()).thenReturn(Rule.Kind.MATCH);
        return rule;
    }

    static <T> ReferenceRule<T> mockReferenceRule() {
        @SuppressWarnings("unchecked")
        ReferenceRule<T> rule = mock(ReferenceRule.class);
        when(rule.getKind()).thenReturn(Rule.Kind.REFERENCE);
        return rule;
    }

    static <T> QuantifierRule<T> mockQuantifierRule() {
        @SuppressWarnings("unchecked")
        QuantifierRule<T> rule = mock(QuantifierRule.class);
        when(rule.getKind()).thenReturn(Rule.Kind.QUANTIFIER);
        return rule;
    }

    static <T> SkipRule<T> mockSkipRule() {
        @SuppressWarnings("unchecked")
        SkipRule<T> rule = mock(SkipRule.class);
        when(rule.getKind()).thenReturn(Rule.Kind.SKIP);
        return rule;
    }

    static <T> EmptyRule<T> mockEmptyRule() {
        @SuppressWarnings("unchecked")
        EmptyRule<T> rule = mock(EmptyRule.class);
        when(rule.getKind()).thenReturn(Rule.Kind.EMPTY);
        return rule;
    }

    static <T> Rule.Builder<T> mockRuleBuilder() {
        @SuppressWarnings("unchecked")
        Rule.Builder<T> builder = mock(Rule.Builder.class);
        return builder;
    }

    static <T> SequenceRule.Builder<T> mockSequenceRuleBuilder() {
        @SuppressWarnings("unchecked")
        SequenceRule.Builder<T> builder = mock(SequenceRule.Builder.class);
        return builder;
    }

    static <T> ChoiceRule.Builder<T> mockChoiceRuleBuilder() {
        @SuppressWarnings("unchecked")
        ChoiceRule.Builder<T> builder = mock(ChoiceRule.Builder.class);
        return builder;
    }

    static <T, R, P> RuleVisitor<T, R, P> mockRuleVisitor() {
        @SuppressWarnings("unchecked")
        RuleVisitor<T, R, P> visitor = mock(RuleVisitor.class);
        return visitor;
    }

    static <T> Token<T> mockToken(String name, T value) {
        @SuppressWarnings("unchecked")
        Token<T> token = mock(Token.class);
        when(token.getName()).thenReturn(name);
        when(token.getValue()).thenReturn(value);
        return token;
    }

}
