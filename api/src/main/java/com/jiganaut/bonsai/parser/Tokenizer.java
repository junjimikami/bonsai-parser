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

    public Tokenizer skip(String regex);
    public Tokenizer skip(Pattern pattern);
}
