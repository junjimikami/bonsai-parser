package com.jiganaut.bonsai.parser.impl;

import java.util.Objects;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.TokenizerFactory;

class DefaultTokenizerFactoryProvider implements TokenizerFactoryProvider {

    @Override
    public TokenizerFactory createFactory(Grammar grammar) {
        Objects.requireNonNull(grammar, Message.NULL_PARAMETER.format());
        return new DefaultTokenizerFactory(grammar);
    }
}
