package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.grammar.Rule.Kind;

class EmptyRuleTestCase implements RuleTestCase {

    @Override
    public Rule createTarget() {
        return Rule.EMPTY;
    }

    @Override
    public Kind expectedKind() {
        return Kind.EMPTY;
    }

}
