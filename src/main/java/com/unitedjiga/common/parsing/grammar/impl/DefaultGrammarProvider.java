package com.unitedjiga.common.parsing.grammar.impl;

import java.util.Objects;
import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.ChoiceRule;
import com.unitedjiga.common.parsing.grammar.Grammar;
import com.unitedjiga.common.parsing.grammar.PatternRule;
import com.unitedjiga.common.parsing.grammar.ReferenceRule;
import com.unitedjiga.common.parsing.grammar.SequenceRule;
import com.unitedjiga.common.parsing.grammar.spi.GrammarProvider;

class DefaultGrammarProvider implements GrammarProvider {

    @Override
    public Grammar.Builder createGrammarBuilder() {
        return new DefaultGrammar.Builder();
    }

    @Override
    public PatternRule.Builder createPatternBuilder(String regex) {
        Objects.requireNonNull(regex, Message.NULL_PARAMETER.format());
        return new DefaultPatternRule.Builder(regex);
    }

    @Override
    public PatternRule.Builder createPatternBuilder(Pattern pattern) {
        Objects.requireNonNull(pattern, Message.NULL_PARAMETER.format());
        return new DefaultPatternRule.Builder(pattern);
    }

    @Override
    public SequenceRule.Builder createSequenceBuilder() {
        return new DefaultSequenceRule.Builder();
    }

    @Override
    public ChoiceRule.Builder createChoiceBuilder() {
        return new DefaultChoiceRule.Builder();
    }

    @Override
    public ReferenceRule.Builder createReferenceBuilder(String reference) {
        Objects.requireNonNull(reference, Message.NULL_PARAMETER.format());
        return new DefaultReferenceRule.Builder(reference);
    }

}
