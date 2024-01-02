package com.unitedjiga.common.parsing.impl;

import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.TokenizerFactory;
import com.unitedjiga.common.parsing.grammar.Expression;

class DefaultTokenizerFactory implements TokenizerFactory {
    static class BaseBuilder implements Tokenizer.Builder {
        private Supplier<Tokenizer> supplier;
        BaseBuilder(Supplier<Tokenizer> supplier) {
            this.supplier = supplier;
        }
        BaseBuilder() {
            //
        }
        @Override
        public Tokenizer.Builder set(Reader r) {
            Objects.requireNonNull(r);
            supplier = () -> new DefaultTokenizer(new TokenIterator(r));
            return this;
        }
        @Override
        public Tokenizer.Builder filter(Predicate<Token> p) {
            Objects.requireNonNull(p);
            return new FilterBuilder(this::build, p);
        }
        @Override
        public Tokenizer build() {
            return supplier.get();
        }
    }
    static class FilterBuilder extends BaseBuilder {
        private final Predicate<Token> predicate;
        FilterBuilder(Supplier<Tokenizer> supplier, Predicate<Token> predicate) {
            super(supplier);
            this.predicate = predicate;
        }

        @Override
        public Tokenizer.Builder set(Reader r) {
            throw new IllegalStateException();
        }

        @Override
        public Tokenizer build() {
            var tokenizer = super.build();
            var it = new PredicateTokenIterator(tokenizer, predicate);
            return new DefaultTokenizer(it);
        }
    }
    class Builder extends BaseBuilder {
        @Override
        public Tokenizer build() {
            var tokenizer = super.build();
            var it = new InterpretingTokenIterator(tokenizer, production);
            return new DefaultTokenizer(it);
        }
    }

//    private final List<? extends Production> productions;
    private final Expression production;

    DefaultTokenizerFactory(List<? extends Expression> productions) {
        Objects.requireNonNull(productions);
        this.production = new AltProduction(null, productions);
    }
    DefaultTokenizerFactory(Expression production) {
        Objects.requireNonNull(production);
        this.production = production;
    }

    @Override
    public Tokenizer createTokenizer(Reader r) {
        return createTokenizerBuilder()
                .set(r)
                .build();
    }

    @Override
    public Tokenizer.Builder createTokenizerBuilder() {
        return new Builder();
    }

}
