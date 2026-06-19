package com.jiganaut.bonsai.parser.impl;

import java.io.InputStream;
import java.io.Reader;
import java.util.Objects;
import java.util.stream.Collector;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.BinarySource;
import com.jiganaut.bonsai.parser.ErrorNode;
import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.Position;
import com.jiganaut.bonsai.parser.TextSource;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.TokenizerFactory;
import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 *
 * @author Junji Mikami
 */
public final class DefaultParserProvider extends ParserProvider {

    @Override
    public TextSource createTextSource(Reader reader) {
        Objects.requireNonNull(reader, () -> Message.VALIDATION_PARAMETER_NULL.format("reader"));
        return new DefaultTextSource(reader);
    }

    @Override
    public BinarySource createBinarySource(InputStream inputStream) {
        Objects.requireNonNull(inputStream, () -> Message.VALIDATION_PARAMETER_NULL.format("inputStream"));
        return new DefaultBinarySource(inputStream);
    }

    @Override
    public <T> ParserFactory<T> createParserFactory(Grammar<T> grammar) {
        Objects.requireNonNull(grammar, () -> Message.VALIDATION_PARAMETER_NULL.format("grammar"));
        return new DefaultParserFactory<>(grammar);
    }

    @Override
    public <T, R> TokenizerFactory<T, R> createTokenizerFactory(Grammar<T> grammar, Collector<? super T, ?, R> collector) {
        Objects.requireNonNull(grammar, () -> Message.VALIDATION_PARAMETER_NULL.format("grammar"));
        Objects.requireNonNull(collector, () -> Message.VALIDATION_PARAMETER_NULL.format("collector"));
        return new DefaultTokenizerFactory<>(grammar, collector);
    }

    @Override
    public <T> NonTerminalNode.Builder<T> createNonTerminalNodeBuilder(String name) {
        Objects.requireNonNull(name, () -> Message.VALIDATION_PARAMETER_NULL.format("name"));
        return new DefaultNonTerminalNode.Builder<>(name);
    }

    @Override
    public <T> Token<T> createToken(String name, T value, Position position) {
        Objects.requireNonNull(value, () -> Message.VALIDATION_PARAMETER_NULL.format("value"));
        Objects.requireNonNull(position, () -> Message.VALIDATION_PARAMETER_NULL.format("position"));
        return new DefaultToken<>(name, value, position);
    }

    @Override
    public <T> ErrorNode.Builder<T> createErrorNodeBuilder() {
        return new DefaultErrorNode.Builder<>();
    }

}
