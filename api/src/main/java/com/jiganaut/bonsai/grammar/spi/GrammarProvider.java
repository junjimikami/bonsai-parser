package com.jiganaut.bonsai.grammar.spi;

import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.SequenceRule;

public interface GrammarProvider {
    Grammar.Builder createGrammarBuilder();

    PatternRule.Builder createPatternBuilder(String regex);

    PatternRule.Builder createPatternBuilder(Pattern pattern);

    SequenceRule.Builder createSequenceBuilder();

    ChoiceRule.Builder createChoiceBuilder();

    ReferenceRule.Builder createReferenceBuilder(String reference);

}