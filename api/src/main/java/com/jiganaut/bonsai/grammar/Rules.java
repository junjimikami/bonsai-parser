package com.jiganaut.bonsai.grammar;

import java.util.regex.Pattern;

public final class Rules {

    private Rules() {
    }

    public static PatternRule pattern(String regex) {
        return PatternRule.of(regex);
    }

    public static PatternRule pattern(Pattern pattern) {
        return PatternRule.of(pattern);
    }

    public static SequenceRule concat(Rule... rules) {
        return SequenceRule.of(rules);
    }

    public static ChoiceRule oneOf(Rule... choices) {
        return ChoiceRule.of(choices);
    }

    public static ReferenceRule reference(String reference) {
        return ReferenceRule.of(reference);
    }

    public static PatternRule quote(String str) {
        return PatternRule.of(Pattern.quote(str));
    }

    public static Rule empty() {
        return Rule.EMPTY;
    }

}
