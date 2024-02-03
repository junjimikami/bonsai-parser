package com.unitedjiga.common.parsing.grammar;

import java.util.regex.Pattern;

public final class Rules {

    private Rules() {
    }

    public static PatternRule.Builder pattern(String regex) {
        return PatternRule.builder(regex);
    }

    public static PatternRule.Builder pattern(Pattern pattern) {
        return PatternRule.builder(pattern);
    }

    public static SequenceRule.Builder sequenceBuilder() {
        return SequenceRule.builder();
    }

    public static ChoiceRule.Builder choiceBuilder() {
        return ChoiceRule.builder();
    }

    public static ReferenceRule.Builder reference(String reference) {
        return ReferenceRule.builder(reference);
    }

}
