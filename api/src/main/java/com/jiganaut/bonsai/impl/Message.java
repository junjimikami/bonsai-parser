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
     * {0}: ProductionSet
     * {1}: Token
     * {2}: Line number
     * {3}: Index
     */
    NO_MATCHING_PRODUCTION_RULE("""
            No matching production rule found.
            Expected: {0}
            Token: "{1}" at line {2}, index {3}
            """),
    AMBIGUOUS_GRAMMAR("""
            Grammar is ambiguous.
            Found: {0}
            Token: "{1}" at line {2}, index {3}
            """),
    NO_MATCHING_RULE("""
            No matching rule found.
            Expected: {0} in production rule {1}
            Token: "{2}" at line {3}, index {4}
            """),
    AMBIGUOUS_CHOICE("""
            Choice rule is ambiguous.
            Found: {0} in production rule {1}
            Token: "{2}" at line {3}, index {4}
            """),
    TOKENS_REMAINING("""
            Tokens still remain.
            Token: "{0}" at line {1}, index {2}
            """),

//    TOKEN_NOT_MATCH_PATTERN("Token did not match pattern {0}."),
//    NO_MATCHING_RULE("In production rule {0}, {1} was expected, but {2} was found."),
    /**
     * 0: Rule
     * 1: Production rule symbol
     * 2: Next token
     * 3: Line number
     * 4: Index
     */
//    NO_MATCHING_RULE("Token did not match the production rule.\n"
//            + "Expected: {0} (Production rule: {1})\n"
//            + "Found: {2} (Line: {3}, Index: {4})"),
//    AMBIGUOUS_CHOICE("In production rule {0}, the choice rule {1} is ambiguous."),
//    AMBIGUOUS_CHOICE("The choice of {0} in production rule {1} is ambiguous."),
//    AMBIGUOUS_PRODUCTION_SET("The production rule {0} is ambiguous."),
//    TOKEN_COUNT_OUT_OF_RANGE("In production rule {0}, {1} was expected to occur {2} times, but it occurred {3} times."),
    /**
     * 0: Min count
     * 1: Max count
     * 2: Rule
     * 3: Production rule symbol
     * 4: Actual count
     * 5: Next token
     * 6: Line number
     * 7: Index
     */
//    TOKEN_COUNT_OUT_OF_RANGE("Token count was out of range.\n"
//            + "Expected: {0}-{1} time{1,choice,0#s|1#|1<s} {2} (Production rule: {3})\n"
//            + "Actual: {4} time{4,choice,0#s|1#|1<s}\n"
//            + "Found: {5} (Line: {6}, Index: {7})"),
//    TOKEN_COUNT_OUT_OF_RANGE_WITHOUT_UPPER_LIMIT("Token count was out of range.\n"
//            + "Expected: {0} or more times {2} (Production rule: {3})\n"
//            + "Actual: {4} time{4,choice,0#s|1#|1<s}\n"
//            + "Found: {5} (Line: {6}, Index: {7})"),
//    TOKEN_COUNT_MISMATCH("Token count was mismatched.\n"
//            + "Expected: {0} time{0,choice,0#s|1#|1<s} {2} (Production rule: {3})\n"
//            + "Actual: {4} time{4,choice,0#s|1#|1<s}\n"
//            + "Found: {5} (Line: {6}, Index: {7})"),
//    TOKENS_REMAINING("EOF was expected, but tokens remained."),
//    TOKENS_REMAINING("Token remained.\n"
//            + "Expected: EOF\n"
//            + "Found: {0} (Line: {1}, Index: {2})"),
    ;

    private final MessageFormat msg;

    private Message(String message) {
        this.msg = new MessageFormat(message);
    }

    public String format(Object... args) {
        return msg.format(args);
    }

}
