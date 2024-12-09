package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.grammar.Rule.Kind;

/**
 * 
 * @author Junji Mikami
 */
class EmptyRuleTest implements RuleTestCase {

    @Override
    public Rule createTarget() {
        return Rule.EMPTY;
    }

    @Override
    public Kind expectedKind() {
        return Kind.EMPTY;
    }

}
