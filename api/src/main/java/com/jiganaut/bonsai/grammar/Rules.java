package com.jiganaut.bonsai.grammar;

import java.util.Objects;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.impl.Message;

/**
 *
 * @author Junji Mikami
 */
public final class Rules {

    private static record NameValueMatchingRule<T>(String name, T value) implements MatchingRule<T> {

        @Override
        public boolean test(String name, T value) {
            return Objects.equals(this.name, name) && Objects.equals(this.value, value);
        }

        @Override
        public String toString() {
            if (value instanceof String) {
                return "%s: \"%s\"".formatted(name, value);
            }
            return "%s: %s".formatted(name, value);
        }

    }

    private static record NameMatchingRule<T>(String name) implements MatchingRule<T> {

        @Override
        public boolean test(String name, T value) {
            return Objects.equals(this.name, name);
        }

        @Override
        public String toString() {
            return name;
        }

    }

    private static record ValueMatchingRule<T>(T value) implements MatchingRule<T> {

        @Override
        public boolean test(String name, T value) {
            return Objects.equals(this.value, value);
        }

        @Override
        public String toString() {
            if (value instanceof String) {
                return "\"%s\"".formatted(value);
            }
            return "%s".formatted(value);
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

    public static <T> MatchingRule<T> token(String name, T value) {
        return new NameValueMatchingRule<>(name, value);
    }

    public static <T> MatchingRule<T> token(String name) {
        return new NameMatchingRule<>(name);
    }

    public static <T> MatchingRule<T> matching(T value) {
        return new ValueMatchingRule<>(value);
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
        Objects.requireNonNull(rules, () -> Message.VALIDATION_PARAMETER_NULL.format("rules"));
        var builder = SequenceRule.<T>builder();
        for (var rule : rules) {
            builder.add(rule);
        }
        return builder.build();
    }

    @SafeVarargs
    public static <T> ChoiceRule<T> oneOf(Rule<T>... choices) {
        Objects.requireNonNull(choices, () -> Message.VALIDATION_PARAMETER_NULL.format("choices"));
        var builder = ChoiceRule.<T>builder();
        for (var choice : choices) {
            builder.add(choice);
        }
        return builder.build();
    }

    @SafeVarargs
    public static <T> ChoiceRule<T> firstOf(Rule<T>... choices) {
        Objects.requireNonNull(choices, () -> Message.VALIDATION_PARAMETER_NULL.format("choices"));
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
