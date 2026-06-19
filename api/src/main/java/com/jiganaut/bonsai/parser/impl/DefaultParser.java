package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.Tree;

/**
 *
 * @author Junji Mikami
 */
class DefaultParser<T> implements Parser<T> {

    private final Context<T> context;
    private final TreeProcessor<T> processor;
    private boolean isParsed;

    DefaultParser(Grammar<T> grammar, Tokenizer<T> tokenizer) {
        assert grammar != null;
        assert tokenizer != null;
        context = new Context<>(grammar, tokenizer);
        processor = new TreeProcessor<>();
    }

    @Override
    public Tree<T> parse() {
        if (isParsed) {
            throw new IllegalStateException(Message.STATE_ALREADY_COMPLETED.format("parse"));
        }
        try {
            return processor.process(context);
        } finally {
            isParsed = true;
        }
    }

    @Override
    public void close() throws IOException {
        context.close();
    }

}
