package com.jiganaut.bonsai.grammar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jiganaut.bonsai.parser.Token;

final class GrammarMockFactory {

    static <T> Grammar<T> mockGrammar() {
        @SuppressWarnings("unchecked")
        Grammar<T> grammar = mock(Grammar.class);
        return grammar;
    }

    static <T> ProductionRule<T> mockProductionRule(String symbol, Rule<T> rule) {
        @SuppressWarnings("unchecked")
        ProductionRule<T> productionRule = mock(ProductionRule.class);
        when(productionRule.getSymbol()).thenReturn(symbol);
        when(productionRule.getRule()).thenReturn(rule);
        return productionRule;
    }

    static <T> ProductionRule<T> mockProductionRule(String symbol) {
        @SuppressWarnings("unchecked")
        ProductionRule<T> productionRule = mock(ProductionRule.class);
        when(productionRule.getSymbol()).thenReturn(symbol);
        return productionRule;
    }

    static <T> Rule<T> mockRule() {
        @SuppressWarnings("unchecked")
        Rule<T> rule = mock(Rule.class);
        when(rule.getKind()).thenReturn(Rule.Kind.MATCH);
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

    static <T, R, P> RuleVisitor<T, R, P> mockVisitor() {
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
