package com.jiganaut.bonsai.parser;

import java.io.Closeable;

/**
 *
 * @author Junji Mikami
 */
public interface Parser<T> extends Closeable {

    public Tree<T> parse();
}
