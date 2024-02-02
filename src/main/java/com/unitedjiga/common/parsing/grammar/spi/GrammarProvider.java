package com.unitedjiga.common.parsing.grammar.spi;

import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.ChoiceExpression;
import com.unitedjiga.common.parsing.grammar.Grammar;
import com.unitedjiga.common.parsing.grammar.PatternExpression;
import com.unitedjiga.common.parsing.grammar.ReferenceExpression;
import com.unitedjiga.common.parsing.grammar.SequenceExpression;

public interface GrammarProvider {
    Grammar.Builder createGrammarBuilder();

    PatternExpression.Builder createPatternBuilder(String regex);

    PatternExpression.Builder createPatternBuilder(Pattern pattern);

    SequenceExpression.Builder createSequenceBuilder();

    ChoiceExpression.Builder createChoiceBuilder();

    ReferenceExpression.Builder createReferenceBuilder(String reference);

}