package com.jiganaut.bonsai.grammar;

import java.util.Objects;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.impl.Message;

/**
 *
 * @author Junji Mikami
 */
public final class Rules {

    private static record NameValueMatchingRule(String name, String value) implements MatchingRule<String> {

        @Override
        public boolean test(String name, String value) {
            return Objects.equals(this.name, name) && Objects.equals(this.value, value);
        }

        @Override
        public String toString() {
            return "%s:\"%s\"".formatted(name, value);
        }

    }

    private static record NameMatchingRule(String name) implements MatchingRule<String> {

        @Override
        public boolean test(String name, String value) {
            return Objects.equals(this.name, name);
        }

        @Override
        public String toString() {
            return name;
        }

    }

    private static record ValueMatchingRule(String value) implements MatchingRule<String> {

        @Override
        public boolean test(String name, String value) {
            return Objects.equals(this.value, value);
        }

        @Override
        public String toString() {
            return "\"%s\"".formatted(value);
        }

    }

    private static record PatternMatchingRule(Pattern pattern) implements MatchingRule<String> {

        @Override
        public boolean test(String name, String value) {
            if (value == null) {
                return false;
            }
            return pattern.matcher(value).matches();
        }

        @Override
        public String toString() {
            return "\"%s\"".formatted(pattern.pattern());
        }

        @Override
        public final boolean equals(Object arg0) {
            if (arg0 instanceof PatternMatchingRule other) {
                return pattern.pattern().equals(other.pattern.pattern())
                        && pattern.flags() == other.pattern.flags();
            }
            return false;
        }

        @Override
        public final int hashCode() {
            return Objects.hash(pattern.pattern(), pattern.flags());
        }

    }

    private Rules() {
    }

    public static MatchingRule<String> token(String name, String value) {
        return new NameValueMatchingRule(name, value);
    }

    public static MatchingRule<String> token(String name) {
        return new NameMatchingRule(name);
    }

    public static MatchingRule<String> matching(String value) {
        return new ValueMatchingRule(value);
    }

    public static MatchingRule<String> pattern(String regex) {
        Objects.requireNonNull(regex, () -> Message.VALIDATION_PARAMETER_NULL.format("regex"));
        var pattern = Pattern.compile(regex);
        return new PatternMatchingRule(pattern);
    }

    public static MatchingRule<String> pattern(Pattern pattern) {
        Objects.requireNonNull(pattern, () -> Message.VALIDATION_PARAMETER_NULL.format("pattern"));
        return new PatternMatchingRule(pattern);
    }

    @SafeVarargs
    public static <T> SequenceRule<T> concat(Rule<T>... rules) {
        var builder = SequenceRule.<T>builder();
        for (var rule : rules) {
            builder.add(rule);
        }
        return builder.build();
    }

    @SafeVarargs
    public static <T> ChoiceRule<T> oneOf(Rule<T>... choices) {
        var builder = ChoiceRule.<T>builder();
        for (var choice : choices) {
            builder.add(choice);
        }
        return builder.build();
    }

    @SafeVarargs
    public static <T> ChoiceRule<T> firstOf(Rule<T>... choices) {
        var builder = ChoiceRule.<T>builder();
        for (var choice : choices) {
            builder.add(choice);
        }
        return builder.asShortCircuit().build();
    }

    public static <T> ReferenceRule<T> reference(String reference) {
        return ReferenceRule.of(reference);
    }

    public static <T> EmptyRule<T> empty() {
        return EmptyRule.empty();
    }

}
