package com.jiganaut.bonsai.parser;

import java.util.regex.Pattern;

/**
 *
 * @author Junji Mikami
 */
public interface Tokenizer {

    public boolean hasNext();
    public boolean hasNext(String regex);
    public boolean hasNext(Pattern pattern);
    public Token next();
    public Token next(String regex);
    public Token next(Pattern pattern);

    public default long getLineNumber() {
        throw new UnsupportedOperationException();
    }
    public default long getIndex() {
        throw new UnsupportedOperationException();
    }
}
