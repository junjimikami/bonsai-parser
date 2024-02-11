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
    TOKEN_NOT_MATCH_RULE("In production rule {0}, {1} was expected, but {2} was found."),
    AMBIGUOUS_CHOICE("In production rule {0}, the choice rule {1} is ambiguous."),
    TOKEN_COUNT_OUT_OF_RANGE("In production rule {0}, {1} was expected to occur {2} times, but it occurred {3} times."),
    TOKENS_REMAINED("EOF was expected, but tokens remained."),
    ;

    private final MessageFormat msg;

    private Message(String message) {
        this.msg = new MessageFormat(message);
    }

    String format(Object... args) {
        return msg.format(args);
    }

}
