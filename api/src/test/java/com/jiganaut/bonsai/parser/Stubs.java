package com.jiganaut.bonsai.parser;

import java.io.Reader;
import java.io.StringReader;

import com.jiganaut.bonsai.grammar.SingleOriginGrammar;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.ChoiceGrammar;
import com.jiganaut.bonsai.grammar.Grammar;

final class Stubs {

    static final SingleOriginGrammar DUMMY_GRAMMAR = SingleOriginGrammar.builder()
            .add("", () -> PatternRule.of(""))
            .build();

    static final Grammar DUMMY_PRODUCTION_SET = ChoiceGrammar.builder()
            .add("", () -> PatternRule.of(""))
            .build();

    static Reader closedReader() {
        var reader = new StringReader("");
        reader.close();
        return reader;
    }
}
