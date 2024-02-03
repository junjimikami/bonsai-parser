package com.jiganaut.bonsai.parser.impl;

import java.util.Set;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.ParseException;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.Tree;

class DefaultParser implements Parser {
    private final Context context;

    DefaultParser(Grammar grammar, Tokenizer tokenizer) {
        assert grammar != null;
        assert tokenizer != null;
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
