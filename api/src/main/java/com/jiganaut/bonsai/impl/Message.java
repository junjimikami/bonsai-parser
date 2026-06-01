package com.jiganaut.bonsai.impl;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author Junji Mikami
 *
 */
public enum Message {

    VALIDATION_PARAMETER_NULL("validation.parameter.null"),
    VALIDATION_PARAMETER_MIN("validation.parameter.min"),
    VALIDATION_RANGE_INVALID("validation.range.invalid"),

    STATE_ALREADY_COMPLETED("state.already.completed"),

    GRAMMAR_SYMBOL_NOT_FOUND("grammar.symbol.not_found"),

    TOKENIZER_NO_MORE_TOKENS("tokenizer.no_more_tokens"),

    PARSER_NO_MATCHING_RULE("parser.no_matching_rule"),
    PARSER_AMBIGUOUS_CHOICE("parser.ambiguous_choice"),
    PARSER_TOKENS_REMAINING("parser.tokens_remaining"),
    ;

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");
    private final String key;

    private Message(String key) {
        this.key = key;
    }

    public String format(Object... args) {
        var pattern = MESSAGES.getString(key);
        return MessageFormat.format(pattern, args);
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
