package com.jiganaut.bonsai.parser.spi;

import java.io.InputStream;
import java.io.Reader;
import java.util.stream.Collector;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.BinarySource;
import com.jiganaut.bonsai.parser.ErrorNode;
import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.TextSource;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.TokenizerFactory;
import com.jiganaut.bonsai.parser.impl.DefaultParserProvider;

/**
 *
 * @author Junji Mikami
 */
public abstract class ParserProvider {
    private static final ParserProvider DEFAULT_PROVIDER = new DefaultParserProvider();

    public static ParserProvider load() {
        return DEFAULT_PROVIDER;
    }

    public abstract TextSource createTextSource(Reader reader);

    public abstract BinarySource createBinarySource(InputStream inputStream);

    public abstract <T> ParserFactory<T> createParserFactory(Grammar<T> grammar);

    public abstract <T, R> TokenizerFactory<T, R> createTokenizerFactory(Grammar<T> grammar, Collector<? super T, ?, R> collector);

    public abstract <T> NonTerminalNode.Builder<T> createNonTerminalNodeBuilder(String name);

    public abstract <T> Token<T> createToken(String name, T value);

    public abstract <T> ErrorNode.Builder<T> createErrorNodeBuilder(String name);

}