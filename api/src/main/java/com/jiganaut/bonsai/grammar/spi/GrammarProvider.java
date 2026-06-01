package com.jiganaut.bonsai.grammar.spi;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.EmptyRule;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.SkipRule;
import com.jiganaut.bonsai.grammar.impl.DefaultGrammarProvider;

/**
 *
 * @author Junji Mikami
 */
public abstract class GrammarProvider {

    private static final GrammarProvider DEFAULT_PROVIDER = new DefaultGrammarProvider();

    public static GrammarProvider load() {
        return DEFAULT_PROVIDER;
    }

    public abstract Grammar.Builder createGrammarBuilder(String startSymbol);

    public abstract SequenceRule.Builder createSequenceBuilder();

    public abstract ChoiceRule.Builder createChoiceBuilder();

    public abstract ReferenceRule createReference(String reference);

    public abstract QuantifierRule createQuantifier(Rule rule, int times);

    public abstract QuantifierRule createQuantifier(Rule rule, int from, int to);

    public abstract SkipRule createSkip(Rule rule);

    public abstract EmptyRule createEmpty();

}