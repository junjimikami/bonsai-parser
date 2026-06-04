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

    public abstract <T> Grammar.Builder<T> createGrammarBuilder(String startSymbol);

    public abstract <T> SequenceRule.Builder<T> createSequenceBuilder();

    public abstract <T> ChoiceRule.Builder<T> createChoiceBuilder();

    public abstract <T> ReferenceRule<T> createReference(String reference);

    public abstract <T> QuantifierRule<T> createQuantifier(Rule<T> rule, int times);

    public abstract <T> QuantifierRule<T> createQuantifier(Rule<T> rule, int from, int to);

    public abstract <T> SkipRule<T> createSkip(Rule<T> rule);

    public abstract <T> EmptyRule<T> createEmpty();

}