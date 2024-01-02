package com.unitedjiga.common.parsing.impl;

import java.util.Iterator;
import java.util.stream.Collectors;

import com.unitedjiga.common.parsing.NonTerminalSymbol;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.SymbolVisitor;
import com.unitedjiga.common.parsing.TerminalSymbol;
import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;

class InterpretingTokenIterator implements Iterator<Token> {
    private final SymbolVisitor<Token, Void> visitor = new SymbolVisitor<>() {
        
        @Override
        public Token visitTerminal(TerminalSymbol s, Void p) {
            return new DefaultToken(s.getName(), s.getValue());
        }
        
        @Override
        public Token visitNonTerminal(NonTerminalSymbol s, Void p) {
            var value = s.getSymbols().stream()
                    .map(this::visit)
                    .map(Token::getValue)
                    .collect(Collectors.joining());
            return new DefaultToken(s.getName(), value);
        }
    };

    private final Tokenizer tokenizer;
    private final Production production;
    private final Interpreter interpreter = new Interpreter();

    InterpretingTokenIterator(Tokenizer tokenizer, Production production) {
        this.tokenizer = tokenizer;
        this.production = production;
    }

    @Override
    public boolean hasNext() {
        return tokenizer.hasNext();
    }

    @Override
    public Token next() {
        var s = interpreter.interpret(production, tokenizer);
        return visitor.visit(s);
    }

}