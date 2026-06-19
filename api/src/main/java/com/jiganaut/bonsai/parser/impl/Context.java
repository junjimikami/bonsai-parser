package com.jiganaut.bonsai.parser.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.MatchingRule;
import com.jiganaut.bonsai.grammar.ProductionRule;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
class Context<T> extends CachingTokenizer<T> {

    private final Context<T> parent;
    private final Grammar<T> grammar;
    private final ProductionRule<T> production;
    private final EndOfRule<T> endOfRule;
    private final Supplier<Set<MatchingRule<T>>> followSet;

    Context(Grammar<T> grammar, Tokenizer<T> tokenizer) {
        super(tokenizer);
        assert grammar != null;
        this.parent = null;
        this.grammar = grammar;
        this.production = null;
        this.endOfRule = new EndOfRule<>();
        this.followSet = () -> Set.of(endOfRule);
    }

    private Context(
            Tokenizer<T> tokenizer,
            Cache<T> cache,
            Context<T> parent,
            Grammar<T> grammar,
            ProductionRule<T> production,
            EndOfRule<T> endOfRule,
            Supplier<Set<MatchingRule<T>>> followSet) {
        super(tokenizer, cache);
        assert grammar != null;
        assert endOfRule != null;
        assert followSet != null;
        this.parent = parent;
        this.grammar = grammar;
        this.production = production;
        this.endOfRule = endOfRule;
        this.followSet = followSet;
    }

    Context<T> subContext(ProductionRule<T> production) {
        return new Context<>(
                this.tokenizer,
                this.cache,
                this,
                this.grammar,
                production,
                this.endOfRule,
                this.followSet);
    }

    Context<T> subContext(Supplier<Set<MatchingRule<T>>> followSet) {
        return new Context<>(
                this.tokenizer,
                this.cache,
                this,
                this.grammar,
                null,
                this.endOfRule,
                followSet);
    }

    Context<T> resetPath() {
        return new Context<>(
                this.tokenizer,
                this.cache,
                null,
                this.grammar,
                null,
                this.endOfRule,
                followSet);
    }

    Grammar<T> grammar() {
        return grammar;
    }

    ProductionRule<T> production() {
        if (production != null) {
            return production;
        }
        if (parent != null) {
            return parent.production();
        }
        return null;
    }

    EndOfRule<T> endOfRule() {
        return endOfRule;
    }

    Set<MatchingRule<T>> followSet() {
        return followSet.get();
    }

    List<ProductionRule<T>> productionPath() {
        return Stream.iterate(this, Objects::nonNull, c -> c.parent)
                .map(e -> e.production)
                .filter(Objects::nonNull)
                .toList();
    }

}
