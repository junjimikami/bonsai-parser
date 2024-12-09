package com.jiganaut.bonsai.grammar;

/**
 * 
 * @author Junji Mikami
 */
public interface Production {

    public String getSymbol();
    
    public Rule getRule();
}
