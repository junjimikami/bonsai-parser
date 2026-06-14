package com.jiganaut.bonsai.parser.impl;

import java.io.Reader;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.TextSource;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TokenizerFactory;

/**
 *
 * @author Junji Mikami
 */
class DefaultTextSource extends DefaultSource<String> implements TextSource {

    DefaultTextSource(Reader reader) {
        super(new ReaderTokenizer(reader));
    }

    DefaultTextSource(Tokenizer<String> tokenizer) {
        super(tokenizer);
    }

    @Override
    public TextSource addLayer(Grammar<String> grammar) {
        var tokenizer = TokenizerFactory.of(grammar, Collectors.joining()).createTokenizer(this.tokenizer);
        return new DefaultTextSource(tokenizer);
    }

}
