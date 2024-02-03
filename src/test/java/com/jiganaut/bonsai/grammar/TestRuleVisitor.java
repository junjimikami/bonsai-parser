package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.fail;

interface TestRuleVisitor<R, P> extends SimpleRuleVisitor<R, P> {

    @Override
    default R defaultAction(Rule rule, P p) {
        return fail();
    }
}
