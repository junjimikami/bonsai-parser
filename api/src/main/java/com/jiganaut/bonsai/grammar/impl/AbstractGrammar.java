package com.jiganaut.bonsai.grammar.impl;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.BaseBuilder;
import com.jiganaut.bonsai.impl.Message;

abstract class AbstractGrammar extends DefaultProductionSet implements Grammar {

    static abstract class Builder extends BaseBuilder implements Grammar.Builder {
        final Set<Supplier<Production>> set = new LinkedHashSet<>();

        @Override
        public Grammar.Builder add(String symbol, Rule rule) {
            checkParameter(symbol, rule);
            set.add(() -> new DefaultProduction(symbol, rule));
            return this;
        }

        @Override
        public Grammar.Builder add(String symbol, Rule.Builder builder) {
            checkParameter(symbol, builder);
            set.add(() -> {
                var rule = Objects.requireNonNull(builder.build(), Message.NULL_PARAMETER.format());
                return new DefaultProduction(symbol, rule);
            });
            return this;
        }

        @Override
        protected void checkForBuild() {
            if (set.isEmpty()) {
                throw new IllegalStateException(Message.NO_ELELEMNTS.format());
            }
            super.checkForBuild();
        }

        Set<Production> getProductionSet() {
            var productionSet = set.stream()
                    .map(e -> e.get())
                    .collect(LinkedHashSet<Production>::new, Set::add, Set::addAll);
            ReferenceCheck.run(productionSet);
            CompositeCheck.run(productionSet);
            return productionSet;
        }
    }

    AbstractGrammar(Set<Production> productionSet, boolean shortCircuit) {
        super(productionSet, shortCircuit);
    }

    @Override
    public abstract Grammar shortCircuit();

}
