package com.jiganaut.bonsai.parser;

import java.io.Reader;
import java.io.StringReader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.ProductionSet;

final class Stubs {

    static final Grammar DUMMY_GRAMMAR = Grammar.builder()
            .add("", () -> PatternRule.of(""))
            .build();

    static final ProductionSet DUMMY_PRODUCTION_SET = ProductionSet.builder()
            .add("", () -> PatternRule.of(""))
            .build();

    static Reader closedReader() {
        var reader = new StringReader("");
        reader.close();
        return reader;
    }
}
