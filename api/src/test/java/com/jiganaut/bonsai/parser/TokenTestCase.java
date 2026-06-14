package com.jiganaut.bonsai.parser;

/**
 *
 * @author Junji Mikami
 */
interface TokenTestCase<T> extends TerminalNodeTestCase<T> {

    @Override
    Token<T> createTarget();

}
