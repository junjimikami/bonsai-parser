package com.jiganaut.bonsai.grammar.impl;

import java.text.MessageFormat;

/**
 * @author Junji Mikami
 *
 */
enum Message {

    NULL_PARAMETER("Null was passed to the parameter."),
    ALREADY_BUILT("The builder has already built."),
    NO_ELELEMNTS("No elements have been added to the builder."),
    NEGATIVE_PARAMETER("A negative number was passed to the quantifier parameter."),
    INVALID_MAX_COUNT("The max count is lower than the min count."),
    NO_SUCH_SYMBOL("No production rule named symbol {0} was found."),
    NULL_BUILD_RESULT("The build result of production rule {0} was null.")
    ;

    private final MessageFormat msg;

    private Message(String message) {
        this.msg = new MessageFormat(message);
    }

    String format(Object... args) {
        return msg.format(args);
    }

}
