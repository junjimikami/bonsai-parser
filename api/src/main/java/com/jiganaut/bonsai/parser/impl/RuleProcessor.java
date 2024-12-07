package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.RuleVisitor;

interface RuleProcessor<R, P> extends RuleVisitor<R, P> {

    @Override
    default R visitChoice(ChoiceRule choice, P p) {
        if (choice.isShortCircuit()) {
            return visitChoiceAsShortCircuit(choice, p);
        }
        return visitChoiceAsNotShortCircuit(choice, p);
    }

    R visitChoiceAsShortCircuit(ChoiceRule choice, P p);

    R visitChoiceAsNotShortCircuit(ChoiceRule choice, P p);

}
