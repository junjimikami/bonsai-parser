package com.jiganaut.bonsai.parser;

import java.io.Closeable;
import java.util.Iterator;

/**
 *
 * @author Junji Mikami
 */
public interface Tokenizer extends Closeable, Iterator<Token> {

    public boolean hasNext();
    public Token next();

}
