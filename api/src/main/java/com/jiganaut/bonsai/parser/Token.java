package com.jiganaut.bonsai.parser;

/**
 *
 * @author Junji Mikami
 */
public interface Token extends TerminalNode {

    public String getName();
    public String getValue();

    @Override
    public String toString();
}
