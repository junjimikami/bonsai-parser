package com.jiganaut.bonsai.parser;

/**
 *
 * @author Junji Mikami
 */
public interface Token extends Terminal {

    public String getValue();

    @Override
    public String toString();
}