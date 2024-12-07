package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;

interface ProductionProcessor<R, P> {

    R visitProduction(Production production, P p);

    default R visitProductionSet(ProductionSet productionSet, P p) {
        if (productionSet.isShortCircuit()) {
            return visitProductionSetAsShortCircuit(productionSet, p);
        }
        return visitProductionSetAsNotShortCircuit(productionSet, p);
    }

    R visitProductionSetAsShortCircuit(ProductionSet productionSet, P p);

    R visitProductionSetAsNotShortCircuit(ProductionSet productionSet, P p);

}
