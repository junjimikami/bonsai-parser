package com.jiganaut.bonsai.parser;

import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 *
 * @author Junji Mikami
 */
public interface Token extends TerminalNode {

    public static Token of(String name, String value) {
        return ParserProvider.load().createToken(name, value);
    }

    public static Token ofUnnamed(String value) {
        return ParserProvider.load().createToken(null, value);
    }

}
