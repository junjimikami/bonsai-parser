package com.jiganaut.bonsai.grammar;

import java.util.Set;

public interface ProductionSet extends Set<Production> {

    public boolean isShortCircuit();
}
