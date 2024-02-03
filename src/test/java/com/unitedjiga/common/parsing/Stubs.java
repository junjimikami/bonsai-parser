package com.unitedjiga.common.parsing;

import java.io.Reader;
import java.io.StringReader;

import com.unitedjiga.common.parsing.grammar.Rules;
import com.unitedjiga.common.parsing.grammar.Grammar;

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
