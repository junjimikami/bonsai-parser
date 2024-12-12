package com.jiganaut.bonsai.impl;

import java.text.MessageFormat;

/**
 * @author Junji Mikami
 *
 */
public enum Message {

    ALREADY_BUILT("Already built."),
    SYMBOL_ADDED_VISIBLE("Symbol \"{0}\" has already been added as visible."),
    SYMBOL_NOT_FOUND("Symbol \"{0}\" not found."),
    EMPTY_GRAMMAR("Grammar is empty."),
    EMPTY_CHOICE("Choice rule is empty."),
    EMPTY_SEQUENCE("Sequence rule is empty."),
    NULL_BUILD("Build returned null."),
    NULL_PARAMETER("Parameter cannot be null."),
    NEGATIVE_QUANTIFIER("Quantifier is negative."),
    INVALID_RANGE("Invalid range."),

    NAME_NOT_SET("Name not set."),
    VALUE_NOT_SET("Value not set."),
    ALREADY_PARSED("Already parsed."),
    FACTORY_NOT_FOUND("Factory \"{0}\" not found."),
    NO_TOKENS_REMAINING("No tokens remaining."),

    /**
     * {0}: ProductionSet {1}: Token {2}: Line number {3}: Index
     */
    NO_MATCHING_PRODUCTION_RULE("""
            No matching production rule found.
            Expected: {0}
            Token: {1} at line {2}, index {3}
            """),
    AMBIGUOUS_GRAMMAR("""
            Grammar is ambiguous.
            Found: {0}
            Token: {1} at line {2}, index {3}
            """),
    NO_MATCHING_RULE("""
            No matching rule found.
            Expected: {0} in production rule {1}
            Token: {2} at line {3}, index {4}
            """),
    AMBIGUOUS_CHOICE("""
            Choice rule is ambiguous.
            Found: {0} in production rule {1}
            Token: {2} at line {3}, index {4}
            """),
    TOKENS_REMAINING("""
            Tokens still remain.
            Token: {0} at line {1}, index {2}
            """),
    ;

    private final MessageFormat msg;

    private Message(String message) {
        this.msg = new MessageFormat(message);
    }

    public String format(Object... args) {
        return msg.format(args);
    }

    public static String symbolEncode(String s) {
        return "<" + s.replaceAll("\\s", "_")
                .replace("<", "`<`")
                .replace(">", "`>`")
                + ">";
    }

    public static String stringEncode(String s) {
        return "\"" + s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                + "\"";
    }

}
