package com.jiganaut.bonsai.parser.impl;

import java.util.Objects;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.TerminalNode;
import com.jiganaut.bonsai.parser.TokenizerFactory;
import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 * 
 * @author Junji Mikami
 */
public final class DefaultParserProvider extends ParserProvider {

    @Override
    public ParserFactory createParserFactory(Grammar grammar) {
        Objects.requireNonNull(grammar, Message.NULL_PARAMETER.format());
        return new DefaultParserFactory(grammar);
    }

    @Override
    public ParserFactory loadParserFactory(String factoryName) {
        return ServiceLoader.load(ParserFactory.class).stream()
                .filter(p -> p.type().getName().equals(factoryName))
                .map(Provider::get)
                .findFirst()
                .orElseThrow(() -> new ServiceConfigurationError(Message.FACTORY_NOT_FOUND.format(factoryName)));
    }

    @Override
    public TokenizerFactory createTokenizerFactory(Grammar grammar) {
        Objects.requireNonNull(grammar, Message.NULL_PARAMETER.format());
        return new DefaultTokenizerFactory(grammar);
    }

    @Override
    public TokenizerFactory loadTokenizerFactory(String factoryName) {
        return ServiceLoader.load(TokenizerFactory.class).stream()
                .filter(p -> p.type().getName().equals(factoryName))
                .map(Provider::get)
                .findFirst()
                .orElseThrow(() -> new ServiceConfigurationError(Message.FACTORY_NOT_FOUND.format(factoryName)));
    }

    @Override
    public NonTerminalNode.Builder createNonTerminalNodeBuilder() {
        return new DefaultNonTerminalNode.Builder();
    }

    @Override
    public TerminalNode.Builder createTerminalNodeBuilder() {
        return new DefaultToken.Builder();
    }

}
