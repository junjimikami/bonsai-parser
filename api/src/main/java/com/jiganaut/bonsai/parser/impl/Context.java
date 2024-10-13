package com.jiganaut.bonsai.parser.impl;

import java.util.Set;

import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
record Context(ProductionSet productionSet, Production production, Tokenizer tokenizer, Set<Rule> followSet) {

    Context withProduction(Production production) {
        return new Context(this.productionSet, production, this.tokenizer, this.followSet);
    }

    Context withFollowSet(Set<Rule> followSet) {
        return new Context(this.productionSet, this.production, this.tokenizer, followSet);
    }

}
