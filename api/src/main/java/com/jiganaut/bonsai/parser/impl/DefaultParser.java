package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;
import java.io.UncheckedIOException;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.Tree;

/**
 * 
 * @author Junji Mikami
 */
class DefaultParser implements Parser {
    private final Context context;
    private boolean isParsed;

    DefaultParser(Grammar grammar, Tokenizer tokenizer) {
        assert grammar != null;
        assert tokenizer != null;
        context = new Context(grammar, tokenizer);
    }

    @Override
    public Tree parse() {
        if (isParsed) {
            throw new IllegalStateException(Message.ALREADY_PARSED.format());
        }
        try {
            var derivation = new Derivation();
            return derivation.process(context);
        } catch (UncheckedIOException ex) {
            throw new IllegalStateException(ex);
        } finally {
            isParsed = true;
        }
    }

    @Override
    public void close() throws IOException {
        context.close();
    }

}
