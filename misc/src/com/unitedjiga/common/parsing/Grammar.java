package com.unitedjiga.common.parsing;

import java.util.regex.Pattern;

public interface Grammar {

    public String getStartSymbol();
    
    public Pattern getSkipPattern();
    
}
