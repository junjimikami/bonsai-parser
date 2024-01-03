package com.unitedjiga.common.parsing.grammar.impl;

import java.util.Objects;
import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.ChoiceExpression;
import com.unitedjiga.common.parsing.grammar.Grammar;
import com.unitedjiga.common.parsing.grammar.PatternExpression;
import com.unitedjiga.common.parsing.grammar.ReferenceExpression;
import com.unitedjiga.common.parsing.grammar.SequenceExpression;

class DefaultGrammarProvider {

    Grammar.Builder createGrammarBuilder() {
        return new DefaultGrammar.Builder();
    }

    PatternExpression.Builder createPatternBuilder(String regex) {
        Objects.requireNonNull(regex, Message.NON_NULL_REQUIRED.format());
        return new DefaultPatternExpression.Builder(regex);
    }

    PatternExpression.Builder createPatternBuilder(Pattern pattern) {
        Objects.requireNonNull(pattern, Message.NON_NULL_REQUIRED.format());
        return new DefaultPatternExpression.Builder(pattern);
    }

    SequenceExpression.Builder createSequenceBuilder() {
        return new DefaultSequenceExpression.Builder();
    }

    ChoiceExpression.Builder createChoiceBuilder() {
        return new DefaultChoiceExpression.Builder();
    }

    ReferenceExpression.Builder createReferenceBuilder(String reference) {
        Objects.requireNonNull(reference, Message.NON_NULL_REQUIRED.format());
        return new DefaultReferenceExpression.Builder(reference);
    }

}
