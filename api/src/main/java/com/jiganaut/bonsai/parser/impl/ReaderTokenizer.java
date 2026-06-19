package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.Position;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 *
 * @author Junji Mikami
 */
class ReaderTokenizer implements Tokenizer<String> {

    private final PushbackReader reader;
    private String nextToken;
    private long offset = 0;
    private long line = 1;
    private long column = 1;
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

    @Override
    public boolean hasNext() {
        readNext();
        return nextToken != null;
    }

    @Override
    public Token<String> next() {
        readNext();
        if (nextToken == null) {
            throw new NoSuchElementException(Message.TOKENIZER_NO_MORE_TOKENS.format());
        }
        long startOffset = offset;
        long startLine = line;
        long startColumn = column;
        offset += nextToken.length();
        if (lineIncrement != 0) {
            line += lineIncrement;
            lineIncrement = 0;
            column = 1;
        } else {
            column += nextToken.length();
        }
        long endOffset = offset;
        long endLine = line;
        long endColumn = column;
        var position = Position.of(startOffset, startLine, startColumn).withRangeEnd(endOffset, endLine, endColumn);
        var value = nextToken;
        nextToken = null;
        return new DefaultToken<>(null, value, position);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}