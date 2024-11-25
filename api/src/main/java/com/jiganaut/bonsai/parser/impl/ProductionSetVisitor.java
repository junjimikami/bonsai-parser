package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.grammar.ProductionSet;

interface ProductionSetVisitor<R, P> {

    default R visit(ProductionSet ps, P p) {
        if (ps.isShortCircuit()) {
            return visitShortCircuit(ps, p);
        }
        return visitOther(ps, p);
    }

    R visitOther(ProductionSet ps, P p);

    R visitShortCircuit(ProductionSet ps, P p);
}
