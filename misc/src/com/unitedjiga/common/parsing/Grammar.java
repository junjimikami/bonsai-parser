package com.unitedjiga.common.parsing;

import java.util.regex.Pattern;

public interface Grammar {

    public static interface Builder {
        public Grammar.Builder add(String symbol, Expression.Builder builder);
        public Grammar.Builder add(String symbol, String reference);
        public Grammar.Builder addPattern(String symbol, String pattern);
        public Grammar.Builder setSkipPattern(String pattern);
        public Grammar.Builder setStartSymbol(String symbol);
        public Grammar build();
    }

    public String getStartSymbol();
    
    public Pattern getSkipPattern();
    
    public ProductionSet productionSet();
    
    public Production getStart();
}
