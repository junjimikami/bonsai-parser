package com.jiganaut.bonsai.parser;

import java.io.Reader;
import java.io.StringReader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 *
 * @author Junji Mikami
 */
public interface TextSource extends Source<String> {

    public static TextSource of(Reader reader) {
        return ParserProvider.load().createTextSource(reader);
    }

    public static TextSource of(CharSequence text) {
        return of(new StringReader(text.toString()));
    }

    public TextSource addLayer(Grammar<String> grammar);

}
