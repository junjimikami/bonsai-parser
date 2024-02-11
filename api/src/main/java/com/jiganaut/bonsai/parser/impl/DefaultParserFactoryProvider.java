package com.jiganaut.bonsai.parser.impl;

import java.util.Objects;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.ParserFactory;

class DefaultParserFactoryProvider implements ParserFactoryProvider {

    @Override
    public ParserFactory createFactory(Grammar grammar) {
        Objects.requireNonNull(grammar, Message.NULL_PARAMETER.format());
        return new DefaultParserFactory(grammar);
    }
}
