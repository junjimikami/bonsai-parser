package com.jiganaut.bonsai.parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 *
 * @author Junji Mikami
 */
public interface TextSource extends Source {

    public static TextSource of(Reader reader) {
        return ParserProvider.load().createTextSource(reader);
    }

    public static TextSource of(CharSequence text) {
        return of(new StringReader(text.toString()));
    }

    public default TextSource addLayer(Grammar grammar) {
        return addLayer(grammar, Collectors.joining());
    }

    @Override
    public TextSource addLayer(Grammar grammar, Collector<CharSequence, ?, String> collector);

}
