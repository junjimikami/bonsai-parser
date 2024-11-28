package com.jiganaut.bonsai.grammar;

public interface Grammar extends ProductionSet {

    public static interface Builder {
        public Grammar.Builder add(String symbol, Rule rule);

        public Grammar.Builder add(String symbol, Rule.Builder builder);

        public Grammar build();

        public Grammar shortCircuit();
    }

    public ProductionSet productionSet();

    @Override
    public Grammar shortCircuit();
}
