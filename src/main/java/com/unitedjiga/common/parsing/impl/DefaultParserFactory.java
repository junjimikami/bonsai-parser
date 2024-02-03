package com.unitedjiga.common.parsing.impl;

import java.io.Reader;

import com.unitedjiga.common.parsing.Parser;
import com.unitedjiga.common.parsing.ParserFactory;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.grammar.Grammar;

/**
 * @author Junji Mikami
 *
 */
class DefaultParserFactory implements ParserFactory {
    private final Grammar grammar;

    DefaultParserFactory(Grammar grammar) {
        assert grammar != null;
        this.grammar = grammar;
    }

    @Override
    public Parser createParser(Tokenizer tokenizer) {
        assert tokenizer != null;
        return new DefaultParser(grammar, tokenizer);
    }

    @Override
    public Parser createParser(Reader reader) {
        var tokenizer = new ReaderTokenizer(reader);
        return createParser(tokenizer);
    }

}
