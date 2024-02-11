package com.jiganaut.bonsai.parser;

import java.io.Reader;
import java.io.StringReader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Rules;

final class Stubs {

    static final Grammar DUMMY_GRAMMAR = Grammar.builder()
            .add("", Rules.pattern(""))
            .build();

    static Reader closedReader() {
        var reader = new StringReader("");
        reader.close();
        return reader;
    }
}
