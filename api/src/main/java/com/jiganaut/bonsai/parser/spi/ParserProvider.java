package com.jiganaut.bonsai.parser.spi;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.TerminalNode;
import com.jiganaut.bonsai.parser.TokenizerFactory;
import com.jiganaut.bonsai.parser.impl.DefaultParserProvider;

public abstract class ParserProvider {
    private static final ParserProvider DEFAULT_PROVIDER = new DefaultParserProvider();

    public static ParserProvider load() {
        return DEFAULT_PROVIDER;
    }

    public abstract ParserFactory createParserFactory(Grammar grammar);

    public abstract ParserFactory loadParserFactory(String factoryName);

    public abstract TokenizerFactory createTokenizerFactory(Grammar grammar);

    public abstract TokenizerFactory loadTokenizerFactory(String factoryName);

    public NonTerminalNode.Builder createNonTerminalNodeBuilder() {
        return DEFAULT_PROVIDER.createNonTerminalNodeBuilder();
    }

    public TerminalNode.Builder createTerminalNodeBuilder() {
        return DEFAULT_PROVIDER.createTerminalNodeBuilder();
    }

}