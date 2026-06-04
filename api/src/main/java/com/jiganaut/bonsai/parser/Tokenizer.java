package com.jiganaut.bonsai.parser;

import java.io.Closeable;
import java.util.Iterator;

/**
 *
 * @author Junji Mikami
 */
public interface Tokenizer<T> extends Closeable, Iterator<Token<T>> {

    public boolean hasNext();

    public Token<T> next();

}
