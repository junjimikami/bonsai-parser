package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;
import java.util.Set;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.Tree;

class DefaultParser implements Parser {
    private final Context context;

    DefaultParser(Grammar grammar, Tokenizer tokenizer) {
        assert grammar != null;
        assert tokenizer != null;
        var production = grammar.getStartProduction();
        context = new Context(grammar, production, tokenizer, Set.of());
    }

    @Override
    public Tree parse() {
        return Derivation.run(context);
    }

    @Override
    public void close() throws IOException {
        context.close();
    }

}
