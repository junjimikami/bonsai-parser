package com.jiganaut.bonsai.parser.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    private final Set<MatchingRule<T>> followSet;

    Context(Grammar<T> grammar, Tokenizer<T> tokenizer) {
        super(tokenizer);
        assert grammar != null;
        this.parent = null;
        this.grammar = grammar;
        this.production = null;
        this.endOfRule = new EndOfRule<>();
        this.followSet = Set.of(endOfRule);
    }

    private Context(
            Context<T> parent,
            Grammar<T> grammar,
            ProductionRule<T> production,
            Tokenizer<T> tokenizer,
            EndOfRule<T> endOfRule,
            Set<MatchingRule<T>> followSet,
            Cache<T> cache) {
        super(tokenizer, cache);
        assert parent != null;
        assert grammar != null;
        assert tokenizer != null;
        assert followSet != null;
        assert cache != null;
        this.parent = parent;
        this.grammar = grammar;
        this.production = production;
        this.endOfRule = endOfRule;
        this.followSet = followSet;
    }

    Context<T> subContext(ProductionRule<T> production) {
        return new Context<>(
                this,
                this.grammar,
                production,
                this.tokenizer,
                this.endOfRule,
                this.followSet,
                this.cache);
    }

    Context<T> subContext(Set<MatchingRule<T>> followSet) {
        return new Context<>(
                this,
                this.grammar,
                null,
                this.tokenizer,
                this.endOfRule,
                followSet,
                this.cache);
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
        return followSet;
    }

    List<ProductionRule<T>> productionPath() {
        return Stream.iterate(this, Objects::nonNull, c -> c.parent)
                .map(Context::production)
                .filter(Objects::nonNull)
                .toList();
    }

}
