package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Collector;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
class DefaultTokenizer<T, R> implements Tokenizer<R> {

    private final Context<T> context;
    private final TokenProcessor<T, R> processor;
    private Token<R> nextToken;

    DefaultTokenizer(
            Grammar<T> grammar,
            Tokenizer<T> tokenizer,
            Collector<? super T, ?, R> collector) {
        assert grammar != null;
        assert collector != null;
        assert tokenizer != null;
        context = new Context<>(grammar, tokenizer);
        processor = new TokenProcessor<>(collector);
    }

    private void readNext() {
        if (nextToken != null) {
            return;
        }
        nextToken = processor.process(context);
    }

    @Override
    public boolean hasNext() {
        readNext();
        return nextToken != null;
    }

    @Override
    public Token<R> next() {
        readNext();
        if (nextToken == null) {
            throw new NoSuchElementException(Message.TOKENIZER_NO_MORE_TOKENS.format());
        }
        var token = nextToken;
        nextToken = null;
        return token;
    }

    @Override
    public void close() throws IOException {
        context.close();
    }

}
