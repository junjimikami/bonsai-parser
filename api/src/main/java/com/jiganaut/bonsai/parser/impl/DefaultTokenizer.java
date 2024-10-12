package com.jiganaut.bonsai.parser.impl;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
class DefaultTokenizer extends AbstractTokenizer {

    private final Context context;
    private String nextToken;

    DefaultTokenizer(Grammar grammar, Tokenizer tokenizer) {
        assert grammar != null;
        assert tokenizer != null;
        var production = grammar.getStartProduction();
        context = new Context(grammar, production, tokenizer, Set.of());
    }

    private String read() {
        if (nextToken != null) {
            return nextToken;
        }
        while (context.tokenizer().hasNext()) {
            var token = Tokenization.run(context);
            if (!token.isEmpty()) {
                nextToken = token;
                break;
            }
        }
        return nextToken;
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
        return matcher.lookingAt();
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
        if (matcher.matches()) {
            nextToken = null;
            return new DefaultToken(value);
        }
        if (matcher.lookingAt()) {
            value = matcher.group();
            nextToken = nextToken.substring(matcher.end());
            return new DefaultToken(value);
        }
        throw new NoSuchElementException(Message.TOKEN_NOT_MATCH_PATTERN.format(pattern));
    }

    @Override
    public long getLineNumber() {
        return context.tokenizer().getLineNumber();
    }

    @Override
    public long getIndex() {
        return context.tokenizer().getIndex();
    }
}
