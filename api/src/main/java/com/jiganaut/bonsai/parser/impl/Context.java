package com.jiganaut.bonsai.parser.impl;

import java.util.Set;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
record Context(Grammar grammar, Production production, Tokenizer tokenizer, Set<Rule> followSet) {

    Context withProduction(Production production) {
        return new Context(this.grammar, production, this.tokenizer, this.followSet);
    }

    Context withFollowSet(Set<Rule> followSet) {
        return new Context(this.grammar, this.production, this.tokenizer, followSet);
    }

    void skip() {
        if (grammar.getSkipPattern() == null) {
            return;
        }
        if (!tokenizer.hasNext(grammar.getSkipPattern())) {
            return;
        }
        tokenizer.skip(grammar.getSkipPattern());
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
