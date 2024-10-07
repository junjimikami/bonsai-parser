package com.jiganaut.bonsai.grammar.spi;

import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.SequenceRule;

public interface GrammarProvider {
    Grammar.Builder createGrammarBuilder();

    PatternRule createPattern(String regex);

    PatternRule createPattern(Pattern pattern);

    SequenceRule.Builder createSequenceBuilder();

    ChoiceRule.Builder createChoiceBuilder();

    ReferenceRule createReference(String reference);

}