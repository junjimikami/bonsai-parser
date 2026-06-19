package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.grammar.MatchingRule;
import com.jiganaut.bonsai.parser.Token;

/**
 * @author Junji Mikami
 *
 */
final class EndOfRule<T> implements MatchingRule<T> {

    @Override
    public boolean test(String name, T value) {
        return false;
    }

    @Override
    public boolean test(Token<T> token) {
        return token instanceof EndOfToken;
    }

    @Override
    public String toString() {
        return "EOF";
    }

}
