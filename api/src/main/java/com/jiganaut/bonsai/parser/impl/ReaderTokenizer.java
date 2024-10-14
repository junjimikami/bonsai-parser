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
    private String currentToken;
    private long lineNumber = 1;
    private long index = 0;
    private int lineIncrement = 0;

    /**
     * @param reader
     */
    ReaderTokenizer(Reader reader) {
        assert reader != null;
        this.reader = new PushbackReader(reader);
    }

    private void readNext() {
        if (nextToken != null) {
            return;
        }
        try {
            int ch = reader.read();
            if (ch == -1) {
                return;
            }
            final char ch0 = (char) ch;
            if (Character.isHighSurrogate(ch0)) {
                // Continue
            } else if (ch0 == '\r') {
                lineIncrement = 1;
                // Continue
            } else if (ch0 == '\n') {
                lineIncrement = 1;
                nextToken = String.valueOf(ch0);
                return;
            } else {
                nextToken = String.valueOf(ch0);
                return;
            }
            ch = reader.read();
            if (ch == -1) {
                nextToken = String.valueOf(ch0);
                return;
            }
            final char ch1 = (char) ch;
            if (Character.isHighSurrogate(ch0) && Character.isLowSurrogate(ch1)) {
                nextToken = new String(new char[] { ch0, ch1 });
                return;
            } else if (ch0 == '\r' && ch1 == '\n') {
                lineIncrement = 0;
            }
            reader.unread(ch1);
            nextToken = String.valueOf(ch0);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void writeCurrent() {
        assert nextToken != null;
        if (lineIncrement != 0) {
            lineNumber += lineIncrement;
            lineIncrement = 0;
            index = 0;
        } else {
            index += nextToken.length();
        }
        currentToken = nextToken;
        nextToken = null;
    }

    @Override
    public boolean hasNext() {
        readNext();
        return nextToken != null;
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
    public String next() {
        readNext();
        if (nextToken == null) {
            throw new NoSuchElementException(Message.TOKEN_NOT_FOUND.format());
        }
        writeCurrent();
        return null;
    }

    @Override
    public String next(String regex) {
        Objects.requireNonNull(regex, Message.NULL_PARAMETER.format());
        var pattern = Pattern.compile(regex);
        return next(pattern);
    }

    @Override
    public String next(Pattern pattern) {
        Objects.requireNonNull(pattern, Message.NULL_PARAMETER.format());
        readNext();
        if (nextToken == null) {
            throw new NoSuchElementException(Message.TOKEN_NOT_FOUND.format());
        }
        var matcher = pattern.matcher(nextToken);
        if (!matcher.matches()) {
            throw new NoSuchElementException(Message.TOKEN_NOT_MATCH_PATTERN.format(pattern));
        }
        writeCurrent();
        return null;
    }

    @Override
    public Token getToken() {
        return new DefaultToken(null, currentToken);
    }

    @Override
    public String getValue() {
        return currentToken;
    }

    @Override
    public long getLineNumber() {
        return lineNumber;
    }

    @Override
    public long getIndex() {
        return index;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}