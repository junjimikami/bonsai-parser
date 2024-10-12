package com.jiganaut.bonsai.grammar.impl;

import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SkipRule;
import com.jiganaut.bonsai.grammar.Skippable;

/**
 *
 * @author Junji Mikami
 */
interface DefaultSkippableRule extends Rule, Skippable {

    @Override
    default SkipRule skip() {
        return new DefaultSkipRule(this);
    }

}
