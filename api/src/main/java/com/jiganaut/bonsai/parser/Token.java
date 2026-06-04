package com.jiganaut.bonsai.parser;

import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 *
 * @author Junji Mikami
 */
public interface Token<T> extends TerminalNode<T> {

    public static <T> Token<T> of(String name, T value) {
        return ParserProvider.load().createToken(name, value);
    }

    public static <T> Token<T> ofUnnamed(T value) {
        return ParserProvider.load().createToken(null, value);
    }

}
