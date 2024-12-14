package com.jiganaut.bonsai.parser;

import java.io.Closeable;
import java.io.Reader;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.Grammar;

/**
 *
 * @author Junji Mikami
 */
public interface Tokenizer extends Closeable {

    public static Tokenizer of(Grammar grammar, Reader reader) {
        return TokenizerFactory.of(grammar).createTokenizer(reader);
    }

    public static Tokenizer of(Grammar grammar, Tokenizer tokenizer) {
        return TokenizerFactory.of(grammar).createTokenizer(tokenizer);
    }

    public boolean hasNext();
    public boolean hasNextName(String name);
    public boolean hasNextValue(String regex);
    public boolean hasNextValue(Pattern pattern);
    public Token next();
    public String nextName();
    public String nextValue();

    public String getName();
    public String getValue();

    public long getLineNumber();
    public long getIndex();

}
