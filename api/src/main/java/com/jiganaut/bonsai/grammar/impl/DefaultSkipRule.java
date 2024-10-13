package com.jiganaut.bonsai.grammar.impl;

import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SkipRule;

/**
 * @author Junji Mikami
 *
 */
class DefaultSkipRule extends AbstractRule implements SkipRule {

    private final Rule rule;

    DefaultSkipRule(Rule rule) {
        assert rule != null;
        this.rule = rule;
    }

    @Override
    public Rule getRule() {
        return rule;
    }

    @Override
    public String toString() {
        return "skip " + rule.toString();
    }
}
