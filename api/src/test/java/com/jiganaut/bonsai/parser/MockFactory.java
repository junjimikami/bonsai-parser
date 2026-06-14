package com.jiganaut.bonsai.parser;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jiganaut.bonsai.grammar.Grammar;

final class MockFactory {

    static <T> Grammar<T> mockGrammar() {
        @SuppressWarnings("unchecked")
        Grammar<T> grammar = mock(Grammar.class);
        return grammar;
    }

    static <T> Token<T> mockToken() {
        @SuppressWarnings("unchecked")
        Token<T> token = mock(Token.class);
        when(token.getPosition()).thenReturn(Position.UNKNOWN);
        return token;
    }

    static <T> Token<T> mockToken(String name, T value) {
        @SuppressWarnings("unchecked")
        Token<T> token = mock(Token.class);
        when(token.getPosition()).thenReturn(Position.UNKNOWN);
        when(token.getName()).thenReturn(name);
        when(token.getValue()).thenReturn(value);
        return token;
    }

    static <T, R, P> TreeVisitor<T, R, P> mockTreeVisitor() {
        @SuppressWarnings("unchecked")
        TreeVisitor<T, R, P> visitor = mock(TreeVisitor.class);
        return visitor;
    }

    static <T> Tokenizer<T> mockTokenizer() {
        @SuppressWarnings("unchecked")
        Tokenizer<T> tokenizer = mock(Tokenizer.class);
        return tokenizer;
    }

    static <T> NonTerminalNode.Builder<T> mockNonTerminalNodeBuilder() {
        @SuppressWarnings("unchecked")
        NonTerminalNode.Builder<T> builder = mock(NonTerminalNode.Builder.class);
        return builder;
    }

    static <T> NonTerminalNode<T> mockNonTerminalNode() {
        @SuppressWarnings("unchecked")
        NonTerminalNode<T> node = mock(NonTerminalNode.class);
        return node;
    }

}
