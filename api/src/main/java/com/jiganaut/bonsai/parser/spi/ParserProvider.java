package com.jiganaut.bonsai.parser.spi;

import java.io.Reader;
import java.util.stream.Collector;

import com.jiganaut.bonsai.grammar.Grammar;
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

    public abstract ParserFactory createParserFactory(Grammar grammar);

    public abstract TokenizerFactory createTokenizerFactory(Grammar grammar, Collector<CharSequence, ?, String> collector);

    public abstract NonTerminalNode.Builder createNonTerminalNodeBuilder(String name);

    public abstract Token createToken(String name, String value);

    public abstract ErrorNode.Builder createErrorNodeBuilder(String name);

}