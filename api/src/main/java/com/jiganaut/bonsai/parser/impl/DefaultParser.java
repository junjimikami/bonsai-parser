package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;
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
        context = new Context(grammar.productionSet(), production, tokenizer, Set.of());
    }

    @Override
    public Tree parse() {
        var tree = Derivation.derive(context);
        if (context.tokenizer().hasNext()) {
            var message = MessageSupport.tokensRemained(context);
            throw new ParseException(message);
        }
        return tree;
    }

    @Override
    public void close() throws IOException {
        context.tokenizer().close();
    }

}
