package com.jiganaut.bonsai.parser.spi;

import java.io.Reader;
import java.util.ServiceLoader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.impl.DefaultParserFactoryProvider;

public interface ParserFactoryProvider {
    public static ParserFactoryProvider provider() {
        return ServiceLoader.load(ParserFactoryProvider.class)
                .findFirst()
                .orElseGet(DefaultParserFactoryProvider::provider);
    }
    public ParserFactory createFactory(Grammar grammar);
    public ParserFactory loadFactory(String factoryName);

    public default Parser createParser(Grammar grammar, Reader reader) {
        return createFactory(grammar).createParser(reader);
    }
    public default Parser createParser(Grammar grammar, Tokenizer tokenizer) {
        return createFactory(grammar).createParser(tokenizer);
    }
}