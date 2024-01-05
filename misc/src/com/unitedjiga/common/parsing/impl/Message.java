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

    NON_NULL_REQUIRED("Non-null required."),
    NO_SUCH_TOKEN("Token not found."),
    NO_SUCH_TOKEN_MATCHING_PATTERN("No token matching pattern \"{0}\"."),
    TOO_MANY_TOKEN("Expected EOF, but found extra token."),
    NO_MATCHING_TOKEN("No matching token found."),
    AMBIGUOUS_RULE("Ambiguous rule. More than one token matches."),
    OCCURENCES_OUT_OF_RANGE("Number of occurences out of range."),
    UNEXPECTED_OCCURENCES("Unexpected number of occurences."),
    ;

    private MessageFormat msg;

    private Message(String message) {
        this.msg = new MessageFormat(message);
    }

    String format(Object... args) {
        return msg.format(args);
    }

}
