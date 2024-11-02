package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
class DefaultTokenizer extends AbstractTokenizer {

    private final Context context;
    private Token nextToken;
    private Token currentToken;

    DefaultTokenizer(ProductionSet productionSet, Tokenizer tokenizer) {
        assert productionSet != null;
        assert tokenizer != null;
        context = new Context(productionSet, null, tokenizer, Set.of());
    }

    private void readNext() {
        if (nextToken != null) {
            return;
        }
        while (context.tokenizer().hasNext()) {
            var token = Tokenization.run(context);
            if (!token.getValue().isEmpty()) {
                nextToken = token;
                break;
            }
        }
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
        var matcher = pattern.matcher(nextToken.getValue());
        return matcher.matches();
    }

    @Override
    public String next() {
        readNext();
        if (nextToken == null) {
            throw new NoSuchElementException(Message.TOKEN_NOT_FOUND.format());
        }
        currentToken = nextToken;
        nextToken = null;
        return currentToken.getName();
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
        var matcher = pattern.matcher(nextToken.getValue());
        if (!matcher.matches()) {
            throw new NoSuchElementException(Message.TOKEN_NOT_MATCH_PATTERN.format(pattern));
        }
        currentToken = nextToken;
        nextToken = null;
        return currentToken.getName();
    }

    @Override
    public Token getToken() {
        return currentToken;
    }

    @Override
    public String getValue() {
        return currentToken.getValue();
    }

    @Override
    public long getLineNumber() {
        return context.tokenizer().getLineNumber();
    }

    @Override
    public long getIndex() {
        return context.tokenizer().getIndex();
    }

    @Override
    public void close() throws IOException {
        context.tokenizer().close();
    }
}
