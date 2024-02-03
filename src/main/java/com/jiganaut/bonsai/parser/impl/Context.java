package com.jiganaut.bonsai.parser.impl;

import java.util.Set;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
record Context(Production production, Tokenizer tokenizer, Set<Rule> followSet, Pattern skipPattern) {

    Context withProduction(Production production) {
        return new Context(production, this.tokenizer, this.followSet, this.skipPattern);
    }

    Context withFollowSet(Set<Rule> followSet) {
        return new Context(this.production, this.tokenizer, followSet, this.skipPattern);
    }

    void skip() {
        if (skipPattern == null) {
            return;
        }
        if (!tokenizer.hasNext(skipPattern)) {
            return;
        }
        tokenizer.skip(skipPattern);
    }
    boolean preCheck() {
        var rule = production().getRule();
        if (!AnyMatcher.scan(rule, this)) {
            skip();
        }
        return tokenizer().hasNext();
    }

    boolean postCheck() {
        skip();
        return tokenizer().hasNext();
    }

}
