package com.jiganaut.bonsai.parser;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.spi.ParserFactoryProvider;

public interface Parser {

    public static Parser newParser(Grammar grammar, Reader reader) {
        return ParserFactoryProvider.provider().createParser(grammar, reader);
    }
    public static Parser newParser(Grammar grammar, Tokenizer tokenizer) {
        return ParserFactoryProvider.provider().createParser(grammar, tokenizer);
    }

    public Tree parse();
}
