package com.jiganaut.bonsai.grammar;

import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.impl.GrammarProviders;

public interface Grammar {

    public static interface Builder {
        public Grammar.Builder add(String symbol, Rule.Builder builder);
        public Grammar.Builder add(String symbol, String reference);
        public Grammar.Builder setSkipPattern(String regex);
        public Grammar.Builder setSkipPattern(Pattern pattern);
        public Grammar.Builder setStartSymbol(String symbol);
        public Grammar build();
    }

    public static Builder builder() {
        return GrammarProviders.provider().createGrammarBuilder();
    }

    public String getStartSymbol();
    
    public Pattern getSkipPattern();
    
    public ProductionSet productionSet();
    
    public Production getStartProduction();
}
