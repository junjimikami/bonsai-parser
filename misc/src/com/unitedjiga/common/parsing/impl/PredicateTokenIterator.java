package com.unitedjiga.common.parsing.impl;

import java.util.Iterator;
import java.util.function.Predicate;

import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;

class PredicateTokenIterator implements Iterator<Token> {

    private final Tokenizer tokenizer;
    private final Predicate<Token> predicate;

    PredicateTokenIterator(Tokenizer tokenizer, Predicate<Token> predicate) {
        this.tokenizer = tokenizer;
        this.predicate = predicate;
    }

    @Override
    public boolean hasNext() {
        skip();
        return tokenizer.hasNext();
    }

    @Override
    public Token next() {
        skip();
        return tokenizer.next();
    }

    private void skip() {
        while (tokenizer.hasNext()) {
            if (predicate.test(tokenizer.peek())) {
                return;
            }
            tokenizer.read();
        }
    }
}
