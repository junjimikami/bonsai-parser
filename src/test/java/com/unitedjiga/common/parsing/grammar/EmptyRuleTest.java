package com.unitedjiga.common.parsing.grammar;

import com.unitedjiga.common.parsing.grammar.Rule.Kind;

class EmptyRuleTest implements RuleTest {

    @Override
    public Rule build() {
        return Rule.EMPTY;
    }

    @Override
    public Kind kind() {
        return Kind.EMPTY;
    }

    @Override
    public RuleVisitor<Object[], String> visitor() {
        return new TestRuleVisitor<Object[], String>() {
            @Override
            public Object[] visitEmpty(Rule empty, String p) {
                return new Object[] { empty, p };
            }
        };
    }
}
