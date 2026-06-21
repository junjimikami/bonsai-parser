package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.Position;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 * Tracks token positions by UTF-16 code units.
 * <p>
 * Control characters update cursor position as follows:
 * <ul>
 * <li>{@code \b}: undo one logical previous cursor change</li>
 * <li>{@code \r}: move to column 1 on the current line</li>
 * <li>{@code \n}: move to column 1 on the next line</li>
 * <li>{@code \r\n}: treated as one logical change for {@code \b} undo</li>
 * </ul>
 *
 * @author Junji Mikami
 */
class ReaderTokenizer implements Tokenizer<String> {

    private enum PositionChangeKind {
        CHARACTER,
        CARRIAGE_RETURN,
        LINE_FEED,
        CARRIAGE_RETURN_LINE_FEED
    }

    private static final class PositionChange {
        private final long beforeLine;
        private final long beforeColumn;
        private final long afterLine;
        private final long afterColumn;
        private final PositionChangeKind kind;

        private PositionChange(
                long beforeLine,
                long beforeColumn,
                long afterLine,
                long afterColumn,
                PositionChangeKind kind) {
            this.beforeLine = beforeLine;
            this.beforeColumn = beforeColumn;
            this.afterLine = afterLine;
            this.afterColumn = afterColumn;
            this.kind = kind;
        }
    }

    private final PushbackReader reader;
    private final Deque<PositionChange> positionChanges = new ArrayDeque<>();
    private String nextToken;
    private long offset = 0;
    private long line = 1;
    private long column = 1;

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
            if (!Character.isHighSurrogate(ch0)) {
                nextToken = String.valueOf(ch0);
                return;
            }
            ch = reader.read();
            if (ch == -1) {
                nextToken = String.valueOf(ch0);
                return;
            }
            final char ch1 = (char) ch;
            if (Character.isLowSurrogate(ch1)) {
                nextToken = new String(new char[] { ch0, ch1 });
                return;
            }
            reader.unread(ch1);
            nextToken = String.valueOf(ch0);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void applyPositionChange(String token) {
        if ("\b".equals(token)) {
            if (positionChanges.isEmpty()) {
                return;
            }
            var previous = positionChanges.removeLast();
            line = previous.beforeLine;
            column = previous.beforeColumn;
            return;
        }
        long beforeLine = line;
        long beforeColumn = column;
        long afterLine = beforeLine;
        long afterColumn;
        PositionChangeKind kind;
        if ("\r".equals(token)) {
            afterColumn = 1;
            kind = PositionChangeKind.CARRIAGE_RETURN;
        } else if ("\n".equals(token)) {
            afterLine += 1;
            afterColumn = 1;
            if (!positionChanges.isEmpty() && positionChanges.peekLast().kind == PositionChangeKind.CARRIAGE_RETURN) {
                var previous = positionChanges.removeLast();
                positionChanges.addLast(new PositionChange(
                        previous.beforeLine,
                        previous.beforeColumn,
                        afterLine,
                        afterColumn,
                        PositionChangeKind.CARRIAGE_RETURN_LINE_FEED));
                line = afterLine;
                column = afterColumn;
                return;
            }
            kind = PositionChangeKind.LINE_FEED;
        } else {
            afterColumn = beforeColumn + token.length();
            kind = PositionChangeKind.CHARACTER;
        }
        positionChanges.addLast(new PositionChange(beforeLine, beforeColumn, afterLine, afterColumn, kind));
        line = afterLine;
        column = afterColumn;
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
        var value = nextToken;
        offset += value.length();
        applyPositionChange(value);
        long endOffset = offset;
        long endLine = line;
        long endColumn = column;
        var position = Position.of(startOffset, startLine, startColumn).withRangeEnd(endOffset, endLine, endColumn);
        nextToken = null;
        return new DefaultToken<>(null, value, position);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}