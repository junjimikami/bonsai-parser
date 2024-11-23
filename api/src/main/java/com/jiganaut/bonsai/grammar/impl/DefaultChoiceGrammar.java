package com.jiganaut.bonsai.grammar.impl;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;

import com.jiganaut.bonsai.grammar.ChoiceGrammar;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.Rule;

class DefaultChoiceGrammar extends AbstractGrammar implements ChoiceGrammar {

    static class Builder extends AbstractGrammar.Builder implements ChoiceGrammar.Builder {
        @Override
        public Builder add(String symbol, Rule rule) {
            return (Builder) super.add(symbol, rule);
        }

        @Override
        public Builder add(String symbol, Rule.Builder builder) {
            return (Builder) super.add(symbol, builder);
        }

        @Override
        public ChoiceGrammar build() {
            checkForBuild();
            var productionSet = set.stream()
                    .map(Supplier::get)
                    .collect(LinkedHashSet<Production>::new, Set::add, Set::addAll);
            ReferenceCheck.run(productionSet);
            CompositeCheck.run(productionSet);
            return new DefaultChoiceGrammar(productionSet);
        }

    }

    private DefaultChoiceGrammar(Set<Production> productionSet) {
        super(productionSet);
    }

}
