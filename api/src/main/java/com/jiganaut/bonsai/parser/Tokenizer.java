package com.jiganaut.bonsai.parser;

import java.io.Reader;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.spi.TokenizerFactoryProvider;

/**
 *
 * @author Junji Mikami
 */
public interface Tokenizer {

    public static Tokenizer newTokenizer(Grammar grammar, Reader reader) {
        return TokenizerFactoryProvider.provider().createTokenizer(grammar, reader);
    }
    public static Tokenizer newTokenizer(Grammar grammar, Tokenizer tokenizer) {
        return TokenizerFactoryProvider.provider().createTokenizer(grammar, tokenizer);
    }

    public boolean hasNext();
    public boolean hasNext(String regex);
    public boolean hasNext(Pattern pattern);
    public Token next();
    public Token next(String regex);
    public Token next(Pattern pattern);

    public Tokenizer skip(String regex);
    public Tokenizer skip(Pattern pattern);
    
    public default long getLineNumber() {
        throw new UnsupportedOperationException();
    }
    public default long getIndex() {
        throw new UnsupportedOperationException();
    }
}
