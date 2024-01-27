package com.unitedjiga.common.parsing;

import java.io.Reader;
import java.io.StringReader;

import com.unitedjiga.common.parsing.grammar.Expressions;
import com.unitedjiga.common.parsing.grammar.Grammar;

final class Stubs {

    static final Grammar DUMMY_GRAMMAR = Grammar.builder()
            .add("", Expressions.pattern(""))
            .build();

    static Reader closedReader() {
        var reader = new StringReader("");
        reader.close();
        return reader;
    }
}
