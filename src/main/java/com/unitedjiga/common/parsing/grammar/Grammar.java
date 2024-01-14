package com.unitedjiga.common.parsing.grammar;

import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.impl.GrammarService;

public interface Grammar {

    public static interface Builder {
        public Grammar.Builder add(String symbol, Expression.Builder builder);
        public Grammar.Builder add(String symbol, String reference);
        public Grammar.Builder setSkipPattern(String regex);
        public Grammar.Builder setSkipPattern(Pattern pattern);
        public Grammar.Builder setStartSymbol(String symbol);
        public Grammar build();
    }

    public static Builder builder() {
        return GrammarService.createGrammarBuilder();
    }

    public String getStartSymbol();
    
    public Pattern getSkipPattern();
    
    public ProductionSet productionSet();
    
    public default Production getStart() {
        return productionSet().get(getStartSymbol());
    }
}
