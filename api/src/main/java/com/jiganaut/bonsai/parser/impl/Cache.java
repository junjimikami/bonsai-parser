package com.jiganaut.bonsai.parser.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import com.jiganaut.bonsai.parser.Token;

/**
 *
 * @author Junji Mikami
 */
class Cache<T> {

    static enum State {
        READ, WRITE
    }

    class Cursor {

        private final int mark;

        private Cursor() {
            mark = pos;
            cursors.push(this);
        }

        void reset() {
            pos = mark;
        }

        void clear() {
            if (!cursors.contains(this)) {
                return;
            }
            pos = mark;
            while (cursors.peek() != this) {
                cursors.pop();
            }
            cursors.remove(this);
        }

    }

    private int pos = 0;
    private final int stock;
    private final List<Token<T>> tokens = new ArrayList<>();
    private final Deque<Cursor> cursors = new ArrayDeque<>();

    Cache(int stock) {
        assert 0 <= stock;
        this.stock = stock;
    }

    boolean hasRemaining() {
        return state() == State.READ;
    }

    Token<T> get() {
        assert state() == State.READ;
        var token = tokens.get(pos++);
        if (!cursors.isEmpty()) {
            return token;
        }
        if (stock < pos) {
            tokens.remove(0);
            pos--;
        }
        assert 0 <= pos;
        return token;
    }

    void add(Token<T> token) {
        assert token != null;
        assert state() == State.WRITE;
        tokens.add(pos++, token);
        if (!cursors.isEmpty()) {
            return;
        }
        if (stock < pos) {
            tokens.remove(0);
            pos--;
        }
        assert 0 <= pos;
    }

    State state() {
        if (pos < tokens.size()) {
            return State.READ;
        }
        return State.WRITE;
    }

    Cursor mark() {
        return new Cursor();
    }

    List<Token<T>> buffer() {
        return Collections.unmodifiableList(tokens.subList(0, pos));
    }

}