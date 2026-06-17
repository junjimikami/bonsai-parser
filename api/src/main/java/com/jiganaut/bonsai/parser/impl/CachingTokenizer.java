package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;
import java.util.Optional;

import com.jiganaut.bonsai.parser.Position;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.Tree;

/**
 * @author Junji Mikami
 *
 */
class CachingTokenizer<T> implements Tokenizer<T> {

    final Tokenizer<T> tokenizer;
    final Cache<T> cache;

    CachingTokenizer(Tokenizer<T> tokenizer, Cache<T> cache) {
        assert tokenizer != null;
        assert cache != null;
        this.tokenizer = tokenizer;
        this.cache = cache;
    }

    CachingTokenizer(Tokenizer<T> tokenizer) {
        this(tokenizer, new Cache<>(1));
    }

    @Override
    public boolean hasNext() {
        return cache.hasRemaining() || tokenizer.hasNext();
    }

    @Override
    public Token<T> next() {
        if (cache.hasRemaining()) {
            var token = cache.get();
            return token;
        }
        var token = tokenizer.next();
        if (cache.state() == Cache.State.WRITE) {
            cache.add(token);
        }
        return token;
    }

    @Override
    public void close() throws IOException {
        tokenizer.close();
    }

    Cache<T>.Cursor startCache() {
        return cache.mark();
    }

    Token<T> peek() {
        var cursor = cache.mark();
        try {
            if (cache.hasRemaining()) {
                return cache.get();
            }
            if (tokenizer.hasNext()) {
                var token = tokenizer.next();
                cache.add(token);
                return token;
            }
            var position = last()
                    .map(Tree::getPosition)
                    .map(Position::rangeEndOf)
                    .map(Position::collapse)
                    .orElse(Position.UNKNOWN);
            return new EndOfToken<>(position);
        } finally {
            cursor.clear();
        }
    }

    Optional<Token<T>> last() {
        return Optional.ofNullable(cache.last());
    }

}
