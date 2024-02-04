package com.jiganaut.bonsai.grammar.impl;

import java.util.Objects;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

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
