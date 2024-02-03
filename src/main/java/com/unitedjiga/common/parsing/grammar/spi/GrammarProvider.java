package com.unitedjiga.common.parsing.grammar.spi;

import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.ChoiceRule;
import com.unitedjiga.common.parsing.grammar.Grammar;
import com.unitedjiga.common.parsing.grammar.PatternRule;
import com.unitedjiga.common.parsing.grammar.ReferenceRule;
import com.unitedjiga.common.parsing.grammar.SequenceRule;

public interface GrammarProvider {
    Grammar.Builder createGrammarBuilder();

    PatternRule.Builder createPatternBuilder(String regex);

    PatternRule.Builder createPatternBuilder(Pattern pattern);

    SequenceRule.Builder createSequenceBuilder();

    ChoiceRule.Builder createChoiceBuilder();

    ReferenceRule.Builder createReferenceBuilder(String reference);

}