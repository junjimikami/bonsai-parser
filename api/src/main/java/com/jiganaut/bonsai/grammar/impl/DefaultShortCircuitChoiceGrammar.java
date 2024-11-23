package com.jiganaut.bonsai.grammar.impl;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;

import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.ShortCircuitChoiceGrammar;

class DefaultShortCircuitChoiceGrammar extends DefaultChoiceGrammar implements ShortCircuitChoiceGrammar {

    static class Builder extends DefaultChoiceGrammar.Builder implements ShortCircuitChoiceGrammar.Builder {
        @Override
        public Builder add(String symbol, Rule rule) {
            return (Builder) super.add(symbol, rule);
        }

        @Override
        public Builder add(String symbol, Rule.Builder builder) {
            return (Builder) super.add(symbol, builder);
        }

        @Override
        public ShortCircuitChoiceGrammar build() {
            checkForBuild();
            var productionSet = set.stream()
                    .map(Supplier::get)
                    .collect(LinkedHashSet<Production>::new, Set::add, Set::addAll);
            ReferenceCheck.run(productionSet);
            CompositeCheck.run(productionSet);
            return new DefaultShortCircuitChoiceGrammar(productionSet);
        }

    }

    private DefaultShortCircuitChoiceGrammar(Set<Production> productionSet) {
        super(productionSet);
    }

}
