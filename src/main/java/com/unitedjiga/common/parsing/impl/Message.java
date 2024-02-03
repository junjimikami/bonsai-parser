/*
 * The MIT License
 *
 * Copyright 2021 Junji Mikami.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.unitedjiga.common.parsing.impl;

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
