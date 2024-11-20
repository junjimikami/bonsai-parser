package com.jiganaut.bonsai.grammar.spi;

import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.ShortCircuitChoiceRule;
import com.jiganaut.bonsai.grammar.impl.DefaultGrammarProvider;

public abstract class GrammarProvider {

    private static final GrammarProvider DEFAULT_PROVIDER = new DefaultGrammarProvider();

    public static GrammarProvider load() {
        return DEFAULT_PROVIDER;
    }

    public abstract Grammar.Builder createGrammarBuilder();

    public abstract ProductionSet.Builder createProductionSetBuilder();

    public abstract PatternRule createPattern(String regex);

    public abstract PatternRule createPattern(Pattern pattern);

    public abstract SequenceRule.Builder createSequenceBuilder();

    public abstract ChoiceRule.Builder createChoiceBuilder();

    public abstract ReferenceRule createReference(String reference);

    public abstract ShortCircuitChoiceRule.Builder createShortCircuitChoiceBuilder();

}