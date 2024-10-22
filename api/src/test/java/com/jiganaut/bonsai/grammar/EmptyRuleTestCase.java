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

    @Override
    public RuleVisitor<Object[], String> createVisitor() {
        return new TestRuleVisitor<Object[], String>() {
            @Override
            public Object[] visitEmpty(Rule empty, String p) {
                return new Object[] { empty, p };
            }
        };
    }
}
