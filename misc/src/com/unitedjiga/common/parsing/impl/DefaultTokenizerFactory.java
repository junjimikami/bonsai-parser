package com.unitedjiga.common.parsing.impl;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.Tokenizer.Builder;
import com.unitedjiga.common.parsing.TokenizerFactory;

class DefaultTokenizerFactory implements TokenizerFactory {
    private class Builder extends AbstractTokenizer.DecoratorBuilder {
        @Override
        public Tokenizer build() {
            var tzer = super.build();
            var interpreter = new Interpreter();
            var alt = new AltProduction(null, productions);
            return new AbstractTokenizer(new Iterator<String>() {

                @Override
                public boolean hasNext() {
                    return tzer.hasNext();
                }

                @Override
                public String next() {
                    return interpreter.interpret(alt, tzer)
                            .accept(null, null);
                }
            });
        }
    }

    private final List<? extends Production> productions;

    DefaultTokenizerFactory(List<? extends Production> productions) {
        Objects.requireNonNull(productions);
        this.productions = productions;
    }

    @Override
    public Tokenizer createTokenizer(Reader r) {
        return createTokenizerBuilder()
                .set(r)
                .build();
    }

    @Override
    public Builder createTokenizerBuilder() {
        return new Builder();
    }

}
