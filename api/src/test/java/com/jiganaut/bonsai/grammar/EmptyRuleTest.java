package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.grammar.Rule.Kind;

/**
 *
 * @author Junji Mikami
 */
class EmptyRuleTest implements RuleTestCase<String> {

    @Override
    public Rule<String> createTarget() {
        return EmptyRule.empty();
    }

    @Override
    public Kind expectedKind() {
        return Kind.EMPTY;
    }

}
