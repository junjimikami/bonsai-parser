package com.unitedjiga.common.parsing.impl;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.unitedjiga.common.parsing.NonTerminalSymbol;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.SymbolVisitor;
import com.unitedjiga.common.parsing.TerminalSymbol;
import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.Tokenizer.Builder;
import com.unitedjiga.common.parsing.TokenizerFactory;

class DefaultTokenizerFactory implements TokenizerFactory {
    private class Builder extends DefaultTokenizer.DecoratorBuilder {
        @Override
        public Tokenizer build() {
            var tzer = super.build();
            var interpreter = new Interpreter();
            var alt = new AltProduction(null, productions);
            return new DefaultTokenizer(new Iterator<Token>() {

                @Override
                public boolean hasNext() {
                    return tzer.hasNext();
                }

                @Override
                public Token next() {
                    return interpreter.interpret(alt, tzer)
                            .accept(visitor, null);
                }
            });
        }
    }

    private final SymbolVisitor<Token, Void> visitor = new SymbolVisitor<Token, Void>() {
        
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

    private final List<? extends Production> productions;

    DefaultTokenizerFactory(List<? extends Production> productions) {
        Objects.requireNonNull(productions);
        this.productions = productions;
    }

    @Override
    public Tokenizer createTokenizer(Reader r) {
        return createTokenizerBuilder()
                .set(r)
                .build();
    }

    @Override
    public Builder createTokenizerBuilder() {
        return new Builder();
    }

}
