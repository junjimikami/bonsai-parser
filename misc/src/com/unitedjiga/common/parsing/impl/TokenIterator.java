package com.unitedjiga.common.parsing.impl;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import com.unitedjiga.common.parsing.Token;

class TokenIterator implements Iterator<Token> {
    private final PushbackReader pr;

    TokenIterator(Reader r) {
        Objects.requireNonNull(r);
        pr = new PushbackReader(r);
    }

    @Override
    public boolean hasNext() {
        return peek() != -1;
    }

    @Override
    public Token next() {
        int ch = read();
        if (ch == -1) {
            throw new NoSuchElementException(Message.NO_SUCH_ELEMENT.format());
        }
        return new DefaultToken(Character.toString(ch));
    }

    private int read() {
        try {
            return pr.read();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void unread(int ch) {
        assert ch != -1;
        try {
            pr.unread(ch);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private int peek() {
        int ch = read();
        if (ch != -1) {
            unread(ch);
        }
        return ch;
    }
}