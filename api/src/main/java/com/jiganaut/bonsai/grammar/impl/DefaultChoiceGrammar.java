package com.jiganaut.bonsai.grammar.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.jiganaut.bonsai.grammar.ChoiceGrammar;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.Message;

/**
 * 
 * @author Junji Mikami
 */
class DefaultChoiceGrammar extends AbstractGrammar implements ChoiceGrammar {

    /**
     * 
     * @author Junji Mikami
     */
    static class Builder extends AbstractGrammar.Builder implements ChoiceGrammar.Builder {
        private boolean isHidden = false;
        private Set<String> hiddenSymbols = new HashSet<>();

        private void checkForAdd(String symbol) {
            if (isHidden) {
                if (symbols.contains(symbol)) {
                    throw new IllegalStateException(Message.SYMBOL_ADDED_VISIBLE.format(symbol));
                }
                hiddenSymbols.add(symbol);
            }
        }

        @Override
        public Builder add(String symbol, Rule rule) {
            checkForAdd(symbol);
            return (Builder) super.add(symbol, rule);
        }

        @Override
        public Builder add(String symbol, Rule.Builder builder) {
            checkForAdd(symbol);
            return (Builder) super.add(symbol, builder);
        }

        @Override
        public ChoiceGrammar build() {
            checkForBuild();
            var productionSet = getProductionSet();
            return new DefaultChoiceGrammar(productionSet, false, hiddenSymbols);
        }

        @Override
        public Builder hidden() {
            if (set.isEmpty()) {
                throw new IllegalStateException(Message.EMPTY_GRAMMAR.format());
            }
            isHidden = true;
            check();
            return this;
        }
    }

    private final Set<String> hiddenSymbols;

    DefaultChoiceGrammar(Set<Production> productionSet, boolean shortCircuit, Set<String> hiddenSymbols) {
        super(productionSet, shortCircuit);
        this.hiddenSymbols = Collections.unmodifiableSet(hiddenSymbols);
    }

    @Override
    public Set<String> getHiddenSymbols() {
        return hiddenSymbols;
    }

    @Override
    public ProductionSet productionSet() {
        var set = stream()
                .filter(e -> !getHiddenSymbols().contains(e.getSymbol()))
                .collect(LinkedHashSet<Production>::new, Set::add, Set::addAll);
        return new DefaultProductionSet(set, isShortCircuit());
    }

    @Override
    public ChoiceGrammar shortCircuit() {
        return new DefaultChoiceGrammar(productionSet, true, hiddenSymbols);
    }
}
