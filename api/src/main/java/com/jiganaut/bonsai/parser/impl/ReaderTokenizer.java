package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.parser.Token;

class ReaderTokenizer extends AbstractTokenizer {

    private final PushbackReader reader;
    private String nextToken;

    /**
     * @param reader
     */
    ReaderTokenizer(Reader reader) {
        assert reader != null;
        this.reader = new PushbackReader(reader);
    }

    private String read() {
        if (nextToken != null) {
            return nextToken;
        }
        try {
            int ch0 = reader.read();
            if (ch0 == -1) {
                return null;
            } else if (!Character.isHighSurrogate((char) ch0)) {
                return nextToken = String.valueOf((char) ch0);
            }
            int ch1 = reader.read();
            if (ch1 == -1) {
                return nextToken = String.valueOf((char) ch0);
            } else if (!Character.isLowSurrogate((char) ch1)) {
                reader.unread(ch1);
                return nextToken = String.valueOf((char) ch0);
            }
            return new String(new char[] { (char) ch0, (char) ch1 });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean hasNext() {
        return read() != null;
    }

    @Override
    public boolean hasNext(String regex) {
        Objects.requireNonNull(regex, Message.NULL_PARAMETER.format());
        var pattern = Pattern.compile(regex);
        return hasNext(pattern);
    }

    @Override
    public boolean hasNext(Pattern pattern) {
        Objects.requireNonNull(pattern, Message.NULL_PARAMETER.format());
        if (!hasNext()) {
            return false;
        }
        var matcher = pattern.matcher(nextToken);
        return matcher.matches();
    }

    @Override
    public Token next() {
        var value = read();
        if (value == null) {
            throw new NoSuchElementException(Message.TOKEN_NOT_FOUND.format());
        }
        nextToken = null;
        return new DefaultToken(value);
    }

    @Override
    public Token next(String regex) {
        Objects.requireNonNull(regex, Message.NULL_PARAMETER.format());
        var pattern = Pattern.compile(regex);
        return next(pattern);
    }

    @Override
    public Token next(Pattern pattern) {
        Objects.requireNonNull(pattern, Message.NULL_PARAMETER.format());
        var value = read();
        if (value == null) {
            throw new NoSuchElementException(Message.TOKEN_NOT_FOUND.format());
        }
        var matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            throw new NoSuchElementException(Message.TOKEN_NOT_MATCH_PATTERN.format(pattern));
        }
        nextToken = null;
        return new DefaultToken(value);
    }
}