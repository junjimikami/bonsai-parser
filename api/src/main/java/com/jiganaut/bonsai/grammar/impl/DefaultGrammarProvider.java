package com.jiganaut.bonsai.grammar.impl;

import java.util.Objects;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.ProductionSet.Builder;
import com.jiganaut.bonsai.grammar.spi.GrammarProvider;
import com.jiganaut.bonsai.impl.Message;

class DefaultGrammarProvider implements GrammarProvider {

    @Override
    public Grammar.Builder createGrammarBuilder() {
        return new DefaultGrammar.Builder();
    }

    @Override
    public Builder createProductionSetBuilder() {
        return new DefaultProductionSet.Builder();
    }

    @Override
    public PatternRule createPattern(String regex) {
        Objects.requireNonNull(regex, Message.NULL_PARAMETER.format());
        return new DefaultPatternRule(Pattern.compile(regex));
    }

    @Override
    public PatternRule createPattern(Pattern pattern) {
        Objects.requireNonNull(pattern, Message.NULL_PARAMETER.format());
        return new DefaultPatternRule(pattern);
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
    public ReferenceRule createReference(String reference) {
        Objects.requireNonNull(reference, Message.NULL_PARAMETER.format());
        return new DefaultReferenceRule(reference);
    }

}
