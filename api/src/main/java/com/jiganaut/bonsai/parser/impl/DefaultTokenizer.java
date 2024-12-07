package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
class DefaultTokenizer extends AbstractTokenizer {

    private final Context context;
    private final Tokenization tokenization;
    private String nextTokenName;
    private String nextTokenValue;
    private String currentTokenName;
    private String currentTokenValue;

    DefaultTokenizer(Grammar grammar, Tokenizer tokenizer) {
        assert grammar != null;
        assert tokenizer != null;
        context = new Context(grammar, tokenizer);
        tokenization = new Tokenization();
    }

    private void readNext() {
        if (nextTokenValue != null) {
            return;
        }
        try {
            while (context.hasNext()) {
                var value = tokenization.process(context);
                if (!value.isEmpty()) {
                    nextTokenValue = value;
                    nextTokenName = tokenization.getName();
                    break;
                }
            }
        } catch (UncheckedIOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private void writeCurrent() {
        if (nextTokenValue == null) {
            throw new NoSuchElementException(Message.NO_TOKENS_REMAINING.format());
        }
        currentTokenName = nextTokenName;
        currentTokenValue = nextTokenValue;
        nextTokenName = null;
        nextTokenValue = null;
    }

    @Override
    public boolean hasNext() {
        readNext();
        return nextTokenValue != null;
    }

    @Override
    public boolean hasNextName(String name) {
        if (!hasNext()) {
            return false;
        }
        return Objects.equals(name, nextTokenName);
    }

    @Override
    public boolean hasNextValue(String regex) {
        Objects.requireNonNull(regex, Message.NULL_PARAMETER.format());
        var pattern = Pattern.compile(regex);
        return hasNextValue(pattern);
    }

    @Override
    public boolean hasNextValue(Pattern pattern) {
        Objects.requireNonNull(pattern, Message.NULL_PARAMETER.format());
        if (!hasNext()) {
            return false;
        }
        var matcher = pattern.matcher(nextTokenValue);
        return matcher.matches();
    }

    @Override
    public Token next() {
        readNext();
        writeCurrent();
        return new DefaultToken(currentTokenName, currentTokenValue);
    }

    @Override
    public String nextName() {
        readNext();
        writeCurrent();
        return currentTokenName;
    }

    @Override
    public String nextValue() {
        readNext();
        writeCurrent();
        return currentTokenValue;
    }

    @Override
    public String getName() {
        return currentTokenName;
    }

    @Override
    public String getValue() {
        return currentTokenValue;
    }

    @Override
    public long getLineNumber() {
        return context.getLineNumber();
    }

    @Override
    public long getIndex() {
        return context.getIndex();
    }

    @Override
    public void close() throws IOException {
        context.close();
    }
}
