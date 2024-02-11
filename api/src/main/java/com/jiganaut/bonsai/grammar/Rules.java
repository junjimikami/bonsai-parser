package com.jiganaut.bonsai.grammar;

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

    public static SequenceRule.Builder patternsOf(String... regex) {
        var builder = SequenceRule.builder();
        for (var r : regex) {
            builder.add(PatternRule.builder(r));
        }
        return builder;
    }

    public static ChoiceRule.Builder oneOfPatterns(String... regex) {
        var builder = ChoiceRule.builder();
        for (var r : regex) {
            builder.add(PatternRule.builder(r));
        }
        return builder;
    }

    public static SequenceRule.Builder sequenceBuilder() {
        return SequenceRule.builder();
    }

    public static SequenceRule.Builder sequenceOf(Rule.Builder... builders) {
        var builder = SequenceRule.builder();
        for (var b : builders) {
            builder.add(b);
        }
        return builder;
    }

    public static ChoiceRule.Builder choiceBuilder() {
        return ChoiceRule.builder();
    }

    public static ChoiceRule.Builder oneOf(Rule.Builder... builders) {
        var builder = ChoiceRule.builder();
        for (var b : builders) {
            builder.add(b);
        }
        return builder;
    }

    public static ReferenceRule.Builder reference(String reference) {
        return ReferenceRule.builder(reference);
    }

    public static SequenceRule.Builder referencesOf(String... reference) {
        var builder = SequenceRule.builder();
        for (var r : reference) {
            builder.add(ReferenceRule.builder(r));
        }
        return builder;
    }

    public static ChoiceRule.Builder oneOfreferences(String... reference) {
        var builder = ChoiceRule.builder();
        for (var r : reference) {
            builder.add(ReferenceRule.builder(r));
        }
        return builder;
    }

    public static PatternRule.Builder quote(String str) {
        return PatternRule.builder(Pattern.quote(str));
    }

}
