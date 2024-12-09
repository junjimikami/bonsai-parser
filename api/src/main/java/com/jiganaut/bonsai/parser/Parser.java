package com.jiganaut.bonsai.parser;

import java.io.Closeable;

/**
 * 
 * @author Junji Mikami
 */
public interface Parser extends Closeable {

    public Tree parse();
}
