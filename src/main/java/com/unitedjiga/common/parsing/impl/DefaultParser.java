package com.unitedjiga.common.parsing.impl;

import java.util.Objects;
import java.util.Set;

import com.unitedjiga.common.parsing.Parser;
import com.unitedjiga.common.parsing.ParseException;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.Tree;
import com.unitedjiga.common.parsing.grammar.Grammar;

class DefaultParser implements Parser {
    private final Context context;

    DefaultParser(Grammar grammar, Tokenizer tokenizer) {
        Objects.requireNonNull(grammar);
        Objects.requireNonNull(tokenizer);
        var production = grammar.getStartProduction();
        var skipPattern = grammar.getSkipPattern();
        context = new Context(production, tokenizer, Set.of(), skipPattern);
    }

    @Override
    public Tree parse() {
        var tree = Interpreter.parse(context);
        if (context.postCheck()) {
            throw new ParseException(Message.TOKENS_REMAINED.format());
        }
        return tree;
    }

}
