package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;
import java.io.InputStream;
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
class InputStreamTokenizer implements Tokenizer<Byte> {

    private final InputStream inputStream;
    private Byte nextToken;
    private long offset = 0;

    /**
     * @param inputStream
     */
    InputStreamTokenizer(InputStream inputStream) {
        assert inputStream != null;
        this.inputStream = inputStream;
    }

    private void readNext() {
        if (nextToken != null) {
            return;
        }
        try {
            int b = inputStream.read();
            if (b == -1) {
                return;
            }
            nextToken = (byte) b;
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
    public Token<Byte> next() {
        readNext();
        if (nextToken == null) {
            throw new NoSuchElementException(Message.TOKENIZER_NO_MORE_TOKENS.format());
        }
        long startOffset = offset;
        offset += 1;
        long endOffset = offset;
        var position = Position.of(startOffset).withRangeEnd(endOffset);
        var value = nextToken;
        nextToken = null;
        return new DefaultToken<>(null, value, position);
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}