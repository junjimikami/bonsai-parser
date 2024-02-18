package com.jiganaut.bonsai.parser.impl;

import java.text.MessageFormat;

/**
 * @author Junji Mikami
 *
 */
enum Message {

    NULL_PARAMETER("Null was passed to the parameter."),
    TOKEN_NOT_FOUND("No tokens were found."),
    TOKEN_NOT_MATCH_PATTERN("Token did not match pattern {0}."),
//    TOKEN_NOT_MATCH_RULE("In production rule {0}, {1} was expected, but {2} was found."),
    /**
     * 0: Rule
     * 1: Production rule symbol
     * 2: Next token
     * 3: Line number
     * 4: Index
     */
    TOKEN_NOT_MATCH_RULE("Token did not match the production rule.\n"
            + "Expected: {0} (Production rule: {1})\n"
            + "Found: {2} (Line: {3}, Index: {4})"),
//    AMBIGUOUS_CHOICE("In production rule {0}, the choice rule {1} is ambiguous."),
    AMBIGUOUS_CHOICE("The choice of {0} in production rule {1} is ambiguous."),
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
    TOKEN_COUNT_OUT_OF_RANGE("Token count was out of range.\n"
            + "Expected: {0}-{1} time{1,choice,0#s|1#|1<s} {2} (Production rule: {3})\n"
            + "Actual: {4} time{4,choice,0#s|1#|1<s}\n"
            + "Found: {5} (Line: {6}, Index: {7})"),
    TOKEN_COUNT_OUT_OF_RANGE_WITHOUT_UPPER_LIMIT("Token count was out of range.\n"
            + "Expected: {0} or more times {2} (Production rule: {3})\n"
            + "Actual: {4} time{4,choice,0#s|1#|1<s}\n"
            + "Found: {5} (Line: {6}, Index: {7})"),
    TOKEN_COUNT_MISMATCH("Token count was mismatched.\n"
            + "Expected: {0} time{0,choice,0#s|1#|1<s} {2} (Production rule: {3})\n"
            + "Actual: {4} time{4,choice,0#s|1#|1<s}\n"
            + "Found: {5} (Line: {6}, Index: {7})"),
//    TOKEN_REMAINED("EOF was expected, but tokens remained."),
    TOKEN_REMAINED("Token remained.\n"
            + "Expected: EOF\n"
            + "Found: {0} (Line: {1}, Index: {2})"),
    ;

    private final MessageFormat msg;

    private Message(String message) {
        this.msg = new MessageFormat(message);
    }

    String format(Object... args) {
        return msg.format(args);
    }

}
