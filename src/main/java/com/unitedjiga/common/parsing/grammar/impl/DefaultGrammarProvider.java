package com.unitedjiga.common.parsing.grammar.impl;

import java.util.Objects;
import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.ChoiceExpression;
import com.unitedjiga.common.parsing.grammar.Grammar;
import com.unitedjiga.common.parsing.grammar.PatternExpression;
import com.unitedjiga.common.parsing.grammar.ReferenceExpression;
import com.unitedjiga.common.parsing.grammar.SequenceExpression;
import com.unitedjiga.common.parsing.grammar.spi.GrammarProvider;

class DefaultGrammarProvider implements GrammarProvider {

    @Override
    public Grammar.Builder createGrammarBuilder() {
        return new DefaultGrammar.Builder();
    }

    @Override
    public PatternExpression.Builder createPatternBuilder(String regex) {
        Objects.requireNonNull(regex, Message.NULL_PARAMETER.format());
        return new DefaultPatternExpression.Builder(regex);
    }

    @Override
    public PatternExpression.Builder createPatternBuilder(Pattern pattern) {
        Objects.requireNonNull(pattern, Message.NULL_PARAMETER.format());
        return new DefaultPatternExpression.Builder(pattern);
    }

    @Override
    public SequenceExpression.Builder createSequenceBuilder() {
        return new DefaultSequenceExpression.Builder();
    }

    @Override
    public ChoiceExpression.Builder createChoiceBuilder() {
        return new DefaultChoiceExpression.Builder();
    }

    @Override
    public ReferenceExpression.Builder createReferenceBuilder(String reference) {
        Objects.requireNonNull(reference, Message.NULL_PARAMETER.format());
        return new DefaultReferenceExpression.Builder(reference);
    }

}
