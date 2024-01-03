package com.unitedjiga.common.parsing.grammar;

import java.util.regex.Pattern;

public final class Expressions {

    private Expressions() {
    }

    public static PatternExpression.Builder pattern(String regex) {
        return PatternExpression.builder(regex);
    }

    public static PatternExpression.Builder pattern(Pattern pattern) {
        return PatternExpression.builder(pattern);
    }

    public static SequenceExpression.Builder sequence() {
        return SequenceExpression.builder();
    }

    public static ChoiceExpression.Builder choice() {
        return ChoiceExpression.builder();
    }

    public static ReferenceExpression.Builder reference(String reference) {
        return ReferenceExpression.builder(reference);
    }

}
