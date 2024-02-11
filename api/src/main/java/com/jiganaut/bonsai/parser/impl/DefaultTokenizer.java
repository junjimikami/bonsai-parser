package com.jiganaut.bonsai.parser.impl;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.NonTerminal;
import com.jiganaut.bonsai.parser.Terminal;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TreeVisitor;

/**
 * @author Junji Mikami
 *
 */
class DefaultTokenizer extends AbstractTokenizer {

    private final TreeVisitor<String, Void> treeToString = new TreeVisitor<>() {
        @Override
        public String visitNonTerminal(NonTerminal s, Void p) {
            return s.getSubTrees().stream()
                    .map(this::visit)
                    .collect(Collectors.joining());
        }

        @Override
        public String visitTerminal(Terminal s, Void p) {
            return s.getValue();
        }
    };

    private final Context context;
    private String nextToken;

    DefaultTokenizer(Grammar grammar, Tokenizer tokenizer) {
        assert grammar != null;
        assert tokenizer != null;
        var production = grammar.getStartProduction();
        var skipPattern = grammar.getSkipPattern();
        context = new Context(production, tokenizer, Set.of(), skipPattern);
    }

    private String read() {
        if (nextToken != null) {
            return nextToken;
        }
        if (!context.preCheck()) {
            return null;
        }
        nextToken = Derivation.derive(context)
                .accept(treeToString);
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

}
